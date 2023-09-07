package org.lunatechlabs.dotty.sudoku

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

object SudokuProgressTracker:

  enum Command:
    case NewUpdatesInFlight(count: Int)
    case SudokuDetailState(index: Int, state: ReductionSet)
  export Command.*

  // My responses
  enum Response:
    case Result(sudoku: Sudoku)
  export Response.*

  def apply(
      rowDetailProcessors: Map[Int, ActorRef[SudokuDetailProcessor.Command]],
      sudokuSolver: ActorRef[Response]): Behavior[Command] =
    Behaviors.setup { context =>
      new SudokuProgressTracker(rowDetailProcessors, context, sudokuSolver).trackProgress(updatesInFlight = 0)
    }

class SudokuProgressTracker private (
    rowDetailProcessors: Map[Int, ActorRef[SudokuDetailProcessor.Command]],
    context: ActorContext[SudokuProgressTracker.Command],
    sudokuSolver: ActorRef[SudokuProgressTracker.Response]):

  import SudokuProgressTracker.*

  def trackProgress(updatesInFlight: Int): Behavior[Command] =
    Behaviors.receiveMessage:
      case NewUpdatesInFlight(updateCount) if updatesInFlight - 1 == 0 =>
        context.log.debug("NewUpdatesInFlight({}) - UpdatesInFlight={}", updateCount, updatesInFlight + updateCount)
        rowDetailProcessors.foreach((_, processor) =>
          processor ! SudokuDetailProcessor.GetSudokuDetailState(context.self))

        collectEndState()
      case NewUpdatesInFlight(updateCount) =>
        context.log.debug("NewUpdatesInFlight({}) - UpdatesInFlight={}", updateCount, updatesInFlight + updateCount)
        trackProgress(updatesInFlight + updateCount)
      case msg: SudokuDetailState =>
        context.log.error("Received unexpected message in state 'trackProgress': {}", msg)
        Behaviors.same

  def collectEndState(
      remainingRows: Int = 9,
      endState: Vector[SudokuDetailState] = Vector.empty[SudokuDetailState]): Behavior[Command] =
    Behaviors.receiveMessage:
      case detail: SudokuDetailState if remainingRows == 1 =>
        sudokuSolver ! Result((detail +: endState).sortBy { case SudokuDetailState(idx, _) => idx }.map {
          case SudokuDetailState(_, state) => state
        })
        trackProgress(updatesInFlight = 0)
      case detail: SudokuDetailState =>
        collectEndState(remainingRows = remainingRows - 1, detail +: endState)
      case msg: NewUpdatesInFlight =>
        context.log.error("Received unexpected message in state 'collectEndState': {}", msg)
        Behaviors.same
