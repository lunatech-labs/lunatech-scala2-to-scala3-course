package org.lunatechlabs.dotty.sudoku

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

object SudokuDetailProcessor:

  // My protocol
  enum Command:
    case ResetSudokuDetailState
    case Update(cellUpdates: CellUpdates, replyTo: ActorRef[Response])
    case GetSudokuDetailState(replyTo: ActorRef[SudokuProgressTracker.Command])
  export Command.*

  given CanEqual[Command, Command] = CanEqual.derived

  // My responses
  enum Response:
    case RowUpdate(id: Int, cellUpdates: CellUpdates)
    case ColumnUpdate(id: Int, cellUpdates: CellUpdates)
    case BlockUpdate(id: Int, cellUpdates: CellUpdates)
    case SudokuDetailUnchanged
  export Response.*

  def apply[DetailType <: SudokuDetailType](id: Int, state: ReductionSet = InitialDetailState)(using
      updateSender: UpdateSender[DetailType]): Behavior[Command] =
    Behaviors.setup { context =>
      (new SudokuDetailProcessor[DetailType](context)).operational(id, state, fullyReduced = false)
    }

  trait UpdateSender[A]:
    def sendUpdate(id: Int, cellUpdates: CellUpdates)(using sender: ActorRef[Response]): Unit
    def processorName(id: Int): String

  given UpdateSender[Row] with
    override def sendUpdate(id: Int, cellUpdates: CellUpdates)(using sender: ActorRef[Response]): Unit =
      sender ! RowUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"row-processor-$id"

  given UpdateSender[Column] with
    override def sendUpdate(id: Int, cellUpdates: CellUpdates)(using sender: ActorRef[Response]): Unit =
      sender ! ColumnUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"col-processor-$id"

  given UpdateSender[Block] with
    override def sendUpdate(id: Int, cellUpdates: CellUpdates)(using sender: ActorRef[Response]): Unit =
      sender ! BlockUpdate(id, cellUpdates)
    def processorName(id: Int): String = s"blk-processor-$id"

class SudokuDetailProcessor[DetailType <: SudokuDetailType: SudokuDetailProcessor.UpdateSender] private (
    context: ActorContext[SudokuDetailProcessor.Command]):

  import SudokuDetailProcessor.*

  def operational(id: Int, state: ReductionSet, fullyReduced: Boolean): Behavior[Command] =
    Behaviors.receiveMessage:
      case Update(cellUpdates, replyTo) if !fullyReduced =>
        val previousState = state
        val updatedState = state.mergeState(cellUpdates)
        if updatedState == previousState && cellUpdates != cellUpdatesEmpty then
          replyTo ! SudokuDetailUnchanged
          Behaviors.same
        else
          val transformedUpdatedState = updatedState.applyReductionRuleOne.applyReductionRuleTwo
          if transformedUpdatedState == state then
            replyTo ! SudokuDetailUnchanged
            Behaviors.same
          else
            val updateSender = summon[UpdateSender[DetailType]]
            updateSender.sendUpdate(id, state.stateChanges(transformedUpdatedState))(using replyTo)
            operational(id, transformedUpdatedState, transformedUpdatedState.isFullyReduced)

      case Update(_, replyTo) =>
        replyTo ! SudokuDetailUnchanged
        Behaviors.same

      case GetSudokuDetailState(replyTo) =>
        replyTo ! SudokuProgressTracker.SudokuDetailState(id, state)
        Behaviors.same

      case ResetSudokuDetailState =>
        operational(id, InitialDetailState, fullyReduced = false)
