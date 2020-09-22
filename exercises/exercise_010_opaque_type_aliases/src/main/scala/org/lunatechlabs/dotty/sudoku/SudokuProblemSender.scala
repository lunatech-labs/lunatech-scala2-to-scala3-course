package org.lunatechlabs.dotty.sudoku

import java.io.File

import akka.actor.typed.scaladsl.{ ActorContext, Behaviors, TimerScheduler }
import akka.actor.typed.{ ActorRef, Behavior }

object SudokuProblemSender:

  enum Command:
    case SendNewSudoku
  export Command._

  type CommandAndResponses = Command | SudokuSolver.Response

  private val rowUpdates: Vector[SudokuDetailProcessor.RowUpdate] =
    SudokuIO
      .readSudokuFromFile(new File("sudokus/001.sudoku"))
      .map ((rowIndex, update) => SudokuDetailProcessor.RowUpdate(rowIndex, update))

  def apply(sudokuSolver: ActorRef[SudokuSolver.Command],
            sudokuSolverSettings: SudokuSolverSettings): Behavior[Command] =
    Behaviors.setup[CommandAndResponses] { context =>
      Behaviors.withTimers { timers =>
        new SudokuProblemSender(sudokuSolver, context, timers, sudokuSolverSettings).sending()
      }
    }.narrow // Restrict the actor's [external] protocol to its set of commands

class SudokuProblemSender private (sudokuSolver: ActorRef[SudokuSolver.Command],
                                   context: ActorContext[SudokuProblemSender.CommandAndResponses],
                                   timers: TimerScheduler[SudokuProblemSender.CommandAndResponses],
                                   sudokuSolverSettings: SudokuSolverSettings):
  import SudokuProblemSender._

  private val initialSudokuField = rowUpdates.toSudokuField

  private val rowUpdatesSeq = LazyList
    .continually(
      Vector(
        initialSudokuField,
        initialSudokuField.flipVertically,
        initialSudokuField.flipHorizontally,
        initialSudokuField.flipHorizontally.flipVertically,
        initialSudokuField.flipVertically.flipHorizontally,
        initialSudokuField.columnSwap(0, 1),
        initialSudokuField.rowSwap(4, 5).rowSwap(0, 2),
        initialSudokuField.randomSwapAround,
        initialSudokuField.randomSwapAround,
        initialSudokuField.rotateCW,
        initialSudokuField.rotateCCW,
        initialSudokuField.rotateCW.rotateCW,
        initialSudokuField.transpose,
        initialSudokuField.randomSwapAround,
        initialSudokuField.rotateCW.transpose,
        initialSudokuField.randomSwapAround,
        initialSudokuField.rotateCCW.transpose,
        initialSudokuField.randomSwapAround,
        initialSudokuField.randomSwapAround,
        initialSudokuField.flipVertically.transpose,
        initialSudokuField.flipVertically.rotateCW,
        initialSudokuField.columnSwap(4, 5).columnSwap(0, 2).rowSwap(3, 4),
        initialSudokuField.rotateCW.rotateCW.transpose
      ).map(_.toRowUpdates)
    )
    .flatten
    .iterator

  private val problemSendInterval = sudokuSolverSettings.ProblemSender.SendInterval
  timers.startTimerAtFixedRate(SendNewSudoku,
                               problemSendInterval
  ) // on a 5 node RPi 4 based cluster in steady state, this can be lowered to about 6ms

  def sending(): Behavior[CommandAndResponses] = 
    Behaviors.receiveMessage {
      case SendNewSudoku =>
        context.log.debug("sending new sudoku problem")
        val nextRowUpdates = rowUpdatesSeq.next
        sudokuSolver ! SudokuSolver.InitialRowUpdates(nextRowUpdates, context.self)
        Behaviors.same
      case solution: SudokuSolver.SudokuSolution =>
        context.log.info(s"${SudokuIO.sudokuPrinter(solution)}")
        Behaviors.same
    }
