package org.lunatechlabs.dotty.sudoku

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import org.lunatechlabs.dotty.sudoku.SudokuDetailProcessor.UpdateSender

object SudokuDetailProcessor:

  // My protocol
  sealed trait Command
  case object ResetSudokuDetailState extends Command
  final case class Update(cellUpdates: CellUpdates, replyTo: ActorRef[Response]) extends Command
  final case class GetSudokuDetailState(replyTo: ActorRef[SudokuProgressTracker.Command]) extends Command

  // My responses
  sealed trait Response
  final case class RowUpdate(id: Int, cellUpdates: CellUpdates) extends Response
  final case class ColumnUpdate(id: Int, cellUpdates: CellUpdates) extends Response
  final case class BlockUpdate(id: Int, cellUpdates: CellUpdates) extends Response
  case object SudokuDetailUnchanged extends Response

  val InitialDetailState: ReductionSet = cellIndexesVector.map(_ => initialCell)

  def apply[DetailType <: SudokoDetailType](id: Int,
                                            state: ReductionSet = InitialDetailState)
                                           (implicit updateSender: UpdateSender[DetailType]): Behavior[Command] =
    Behaviors.setup { context =>
      (new SudokuDetailProcessor[DetailType](context)).operational(id, state, fullyReduced = false)
    }

  trait UpdateSender[A]:
    def sendUpdate(id: Int, cellUpdates: CellUpdates)(implicit sender: ActorRef[Response]): Unit
    def processorName(id: Int): String

  implicit val rowUpdateSender: UpdateSender[Row] = new UpdateSender[Row] {
    def sendUpdate(id: Int, cellUpdates: CellUpdates)(implicit sender: ActorRef[Response]): Unit =
      sender ! RowUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"row-processor-$id"
  }

  implicit val columnUpdateSender: UpdateSender[Column] = new UpdateSender[Column] {
    def sendUpdate(id: Int, cellUpdates: CellUpdates)(implicit sender: ActorRef[Response]): Unit =
      sender ! ColumnUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"col-processor-$id"
  }

  implicit val blockUpdateSender: UpdateSender[Block] = new UpdateSender[Block] {
    def sendUpdate(id: Int, cellUpdates: CellUpdates)(implicit sender: ActorRef[Response]): Unit =
      sender ! BlockUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"blk-processor-$id"
  }

class SudokuDetailProcessor[DetailType <: SudokoDetailType : UpdateSender] private (context: ActorContext[SudokuDetailProcessor.Command]):

  import SudokuDetailProcessor._

  def operational(id: Int, state: ReductionSet, fullyReduced: Boolean): Behavior[Command] =
    Behaviors.receiveMessage {
    case Update(cellUpdates, replyTo) if ! fullyReduced =>
      val previousState = state
      val updatedState = mergeState(state, cellUpdates)
      if updatedState == previousState && cellUpdates != cellUpdatesEmpty then
        replyTo ! SudokuDetailUnchanged
        Behaviors.same
      else
        val transformedUpdatedState = updatedState.applyReductionRuleOne.applyReductionRuleTwo
        if transformedUpdatedState == state then
          replyTo ! SudokuDetailUnchanged
          Behaviors.same
        else
          val updateSender = implicitly[UpdateSender[DetailType]]
          updateSender.sendUpdate(id, stateChanges(state, transformedUpdatedState))(replyTo)
          operational(id, transformedUpdatedState, isFullyReduced(transformedUpdatedState))

    case Update(cellUpdates, replyTo) =>
      replyTo ! SudokuDetailUnchanged
      Behaviors.same

    case GetSudokuDetailState(replyTo) =>
      replyTo ! SudokuProgressTracker.SudokuDetailState(id, state)
      Behaviors.same

    case ResetSudokuDetailState =>
      operational(id, InitialDetailState, fullyReduced = false)

  }

  private def mergeState(state: ReductionSet, cellUpdates: CellUpdates): ReductionSet =
      cellUpdates.foldLeft(state) {
      case (stateTally, (index, updatedCellContent)) =>
        stateTally.updated(index, stateTally(index) & updatedCellContent)
    }

  private def stateChanges(state: ReductionSet, updatedState: ReductionSet): CellUpdates =
    (state zip updatedState).zipWithIndex.foldRight(cellUpdatesEmpty) {
      case (((previousCellContent, updatedCellContent), index), cellUpdates)
        if updatedCellContent != previousCellContent =>
        (index, updatedCellContent) +: cellUpdates

      case (_, cellUpdates) => cellUpdates
    }

  private def isFullyReduced(state: ReductionSet): Boolean =
    val allValuesInState = state.flatten
    allValuesInState == allValuesInState.distinct

