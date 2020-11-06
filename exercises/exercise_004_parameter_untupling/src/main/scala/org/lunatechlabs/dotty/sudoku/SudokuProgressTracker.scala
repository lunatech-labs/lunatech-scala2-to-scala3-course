package org.lunatechlabs.dotty.sudoku

import akka.actor.typed.scaladsl.{ ActorContext, Behaviors }
import akka.actor.typed.{ ActorRef, Behavior }

object SudokuProgressTracker:

  sealed trait Command
  final case class NewUpdatesInFlight(count: Int) extends Command
  final case class SudokuDetailState(index: Int, state: ReductionSet) extends Command
  // My responses
  sealed trait Response
  final case class Result(sudoku: Sudoku) extends Response

  def apply(rowDetailProcessors: Map[Int, ActorRef[SudokuDetailProcessor.Command]],
            sudokuSolver: ActorRef[Response]
  ): Behavior[Command] =
    Behaviors.setup { context =>
      new SudokuProgressTracker(rowDetailProcessors, context, sudokuSolver)
        .trackProgress(updatesInFlight = 0)
    }

class SudokuProgressTracker private (
  rowDetailProcessors: Map[Int, ActorRef[SudokuDetailProcessor.Command]],
  context: ActorContext[SudokuProgressTracker.Command],
  sudokuSolver: ActorRef[SudokuProgressTracker.Response]
):

  import SudokuProgressTracker._

  def trackProgress(updatesInFlight: Int): Behavior[Command] =
    Behaviors.receiveMessage {
      case NewUpdatesInFlight(updateCount) if updatesInFlight - 1 == 0 =>
        rowDetailProcessors.foreach ((_, processor) =>
            processor ! SudokuDetailProcessor.GetSudokuDetailState(context.self)
        )
        collectEndState()
      case NewUpdatesInFlight(updateCount) =>
        trackProgress(updatesInFlight + updateCount)
      case msg: SudokuDetailState =>
        context.log.error("Received unexpected message in state 'trackProgress': {}", msg)
        Behaviors.same
    }

  def collectEndState(remainingRows: Int = 9,
                      endState: Vector[SudokuDetailState] = Vector.empty[SudokuDetailState]
  ): Behavior[Command] =
    Behaviors.receiveMessage {
      case detail: SudokuDetailState if remainingRows == 1 =>
        sudokuSolver ! Result(
          (detail +: endState).sortBy { case SudokuDetailState(idx, _) => idx }.map {
            case SudokuDetailState(_, state) => state
          }
        )
        trackProgress(updatesInFlight = 0)
      case detail: SudokuDetailState =>
        collectEndState(remainingRows = remainingRows - 1, detail +: endState)
      case msg: NewUpdatesInFlight =>
        context.log.error("Received unexpected message in state 'collectEndState': {}", msg)
        Behaviors.same
    }
