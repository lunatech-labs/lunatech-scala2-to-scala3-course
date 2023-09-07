package org.lunatechlabs.dotty.sudoku

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, StashBuffer}
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}

import scala.concurrent.duration.*

object SudokuSolver:

  // SudokuSolver Protocol
  enum Command:
    case InitialRowUpdates(
        rowUpdates: Vector[SudokuDetailProcessor.RowUpdate],
        replyTo: ActorRef[SudokuSolver.Response])
  export Command.InitialRowUpdates

  // My Responses
  enum Response:
    case SudokuSolution(sudoku: Sudoku)
  export Response.SudokuSolution

  type CommandAndResponses = Command | SudokuDetailProcessor.Response | SudokuProgressTracker.Response

  given CanEqual[SudokuDetailProcessor.Response, CommandAndResponses] = CanEqual.derived

  import SudokuDetailProcessor.UpdateSender

  def genDetailProcessors[A <: SudokuDetailType: UpdateSender](
      context: ActorContext[CommandAndResponses]): Map[Int, ActorRef[SudokuDetailProcessor.Command]] =
    cellIndexesVector
      .map { index =>
        val detailProcessorName = summon[UpdateSender[A]].processorName(index)
        val detailProcessor = context.spawn(SudokuDetailProcessor(index), detailProcessorName)
        (index, detailProcessor)
      }
      .to(Map)

  def apply(sudokuSolverSettings: SudokuSolverSettings): Behavior[Command] =
    Behaviors
      .supervise[CommandAndResponses]:
        Behaviors.withStash(capacity = sudokuSolverSettings.SudokuSolver.StashBufferSize) { buffer =>
          Behaviors.setup { context =>
            new SudokuSolver(context, buffer).idle()
          }
        }
      .onFailure[Exception](
        SupervisorStrategy.restartWithBackoff(minBackoff = 5.seconds, maxBackoff = 1.minute, randomFactor = 0.2))
      .narrow

class SudokuSolver private (
    context: ActorContext[SudokuSolver.CommandAndResponses],
    buffer: StashBuffer[SudokuSolver.CommandAndResponses]):
  import CellMappings.*
  import SudokuSolver.*

  private val rowDetailProcessors = genDetailProcessors[Row](context)
  private val columnDetailProcessors = genDetailProcessors[Column](context)
  private val blockDetailProcessors = genDetailProcessors[Block](context)
  private val allDetailProcessors =
    List(rowDetailProcessors, columnDetailProcessors, blockDetailProcessors)

  private val progressTracker =
    context.spawn(SudokuProgressTracker(rowDetailProcessors, context.self), "sudoku-progress-tracker")

  def idle(): Behavior[CommandAndResponses] =
    Behaviors.receiveMessage:

      case InitialRowUpdates(rowUpdates, sender) =>
        rowUpdates.foreach { case SudokuDetailProcessor.RowUpdate(row, cellUpdates) =>
          rowDetailProcessors(row) ! SudokuDetailProcessor.Update(cellUpdates, context.self)
        }
        context.log.debug("InitialRowUpdates: {}", rowUpdates)
        progressTracker ! SudokuProgressTracker.NewUpdatesInFlight(rowUpdates.size)
        processRequest(Some(sender), System.currentTimeMillis())
      case unexpectedMsg =>
        context.log.error("Received an unexpected message in 'idle' state: {}", unexpectedMsg)
        Behaviors.same

  def processRequest(requestor: Option[ActorRef[Response]], startTime: Long): Behavior[CommandAndResponses] =
    Behaviors.receiveMessage:
      case SudokuDetailProcessor.RowUpdate(rowNr, updates) =>
        updates.foreach { (rowCellNr, newCellContent) =>
          context.log.debug("Incoming update for Row({},{})={} ", rowNr, rowCellNr, newCellContent)
          val (columnNr, columnCellNr) = rowToColumnCoordinates(rowNr, rowCellNr)
          val columnUpdate = Vector(columnCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from RowProcessor({}) for Column({}, {})={} ",
            rowNr,
            columnNr,
            columnCellNr,
            columnUpdate)
          columnDetailProcessors(columnNr) ! SudokuDetailProcessor.Update(columnUpdate, context.self)

          val (blockNr, blockCellNr) = rowToBlockCoordinates(rowNr, rowCellNr)
          val blockUpdate = Vector(blockCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from RowProcessor({}) for Block({}, {})={} ",
            rowNr,
            blockNr,
            blockCellNr,
            blockUpdate)
          blockDetailProcessors(blockNr) ! SudokuDetailProcessor.Update(blockUpdate, context.self)
        }
        progressTracker ! SudokuProgressTracker.NewUpdatesInFlight(2 * updates.size - 1)
        Behaviors.same
      case SudokuDetailProcessor.ColumnUpdate(columnNr, updates) =>
        updates.foreach { (colCellNr, newCellContent) =>
          context.log.debug("Incoming update for Column({},{})={} ", columnNr, colCellNr, newCellContent)
          val (rowNr, rowCellNr) = columnToRowCoordinates(columnNr, colCellNr)
          val rowUpdate = Vector(rowCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from ColumnProcessor({}) for Row({}, {})={} ",
            columnNr,
            rowNr,
            rowCellNr,
            rowUpdate)
          rowDetailProcessors(rowNr) ! SudokuDetailProcessor.Update(rowUpdate, context.self)

          val (blockNr, blockCellNr) = columnToBlockCoordinates(columnNr, colCellNr)
          val blockUpdate = Vector(blockCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from ColumnProcessor({}) for Block({}, {})={} ",
            columnNr,
            blockNr,
            blockCellNr,
            blockUpdate)
          blockDetailProcessors(blockNr) ! SudokuDetailProcessor.Update(blockUpdate, context.self)
        }
        progressTracker ! SudokuProgressTracker.NewUpdatesInFlight(2 * updates.size - 1)
        Behaviors.same
      case SudokuDetailProcessor.BlockUpdate(blockNr, updates) =>
        updates.foreach { (blockCellNr, newCellContent) =>
          context.log.debug("Incoming update for Block({},{})={} ", blockNr, blockCellNr, newCellContent)
          val (rowNr, rowCellNr) = blockToRowCoordinates(blockNr, blockCellNr)
          val rowUpdate = Vector(rowCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from BlockProcessor({}) for Row({}, {})={} ",
            blockNr,
            rowNr,
            rowCellNr,
            rowUpdate)
          rowDetailProcessors(rowNr) ! SudokuDetailProcessor.Update(rowUpdate, context.self)

          val (columnNr, columnCellNr) = blockToColumnCoordinates(blockNr, blockCellNr)
          val columnUpdate = Vector(columnCellNr -> newCellContent)
          context.log.debug(
            "Outgoing update from BlockProcessor({}) for Column({}, {})={} ",
            blockNr,
            columnNr,
            columnCellNr,
            columnUpdate)
          columnDetailProcessors(columnNr) ! SudokuDetailProcessor.Update(columnUpdate, context.self)
        }
        progressTracker ! SudokuProgressTracker.NewUpdatesInFlight(2 * updates.size - 1)
        Behaviors.same
      case SudokuDetailProcessor.SudokuDetailUnchanged =>
        progressTracker ! SudokuProgressTracker.NewUpdatesInFlight(-1)
        Behaviors.same

      case SudokuProgressTracker.Result(sudoku) =>
        context.log.info(s"Sudoku processing time: ${System.currentTimeMillis() - startTime} milliseconds")
        requestor.get ! SudokuSolution(sudoku)
        resetAllDetailProcessors()
        buffer.unstashAll(idle())

      case msg: InitialRowUpdates if buffer.isFull =>
        context.log.info(s"DROPPING REQUEST")
        Behaviors.same
      case msg: InitialRowUpdates =>
        buffer.stash(msg)
        Behaviors.same

  private def resetAllDetailProcessors(): Unit =
    for
      processors <- allDetailProcessors
      (_, processor) <- processors
    do processor ! SudokuDetailProcessor.ResetSudokuDetailState
