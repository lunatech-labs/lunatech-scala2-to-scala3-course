package org.lunatechlabs.dotty.sudoku

import SudokuDetailProcessor.{Update, SudokuDetailUnchanged, BlockUpdate}
import scala.language.implicitConversions
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import java.nio.file._

class SudokuDetailProcessorSuite extends munit.FunSuite with SudokuTestHelpers {

  val testKit: ActorTestKit = ActorTestKit()

  override def afterAll(): Unit = {
    testKit.shutdownTestKit()
  }

  test("Sending no updates to a sudoku detail processor should result in sending a SudokuDetailUnchanged messsage") {

    val probe = testKit.createTestProbe[SudokuDetailProcessor.Response]()
    val detailProcessor = testKit.spawn(SudokuDetailProcessor[Row](id = 0))
    detailProcessor ! Update(cellUpdatesEmpty, probe.ref)
    probe.expectMessage(SudokuDetailUnchanged)
  }

  test("Sending an update to a fresh instance of the SudokuDetailProcessor that sets one cell to a single value should result in sending an update that reflects this update") {
    val probe = testKit.createTestProbe[SudokuDetailProcessor.Response]()
    val detailProcessor = testKit.spawn(SudokuDetailProcessor[Row](id = 0))
    detailProcessor ! Update(Vector((4, Set(7))), probe.ref)

    val expectedState1 =
      SudokuDetailProcessor.RowUpdate(0, stringToIndexedUpdate(
        Vector(
          "123456 89",
          "123456 89",
          "123456 89",
          "123456 89",
          "      7  ",
          "123456 89",
          "123456 89",
          "123456 89",
          "123456 89"
        ))
      )

    probe.expectMessage(expectedState1)
  }

  test("Sending a series of subsequent Updates to a SudokuDetailProcessor should result in sending updates and ultimately return no changes") {
    val detailParentProbe = testKit.createTestProbe[SudokuDetailProcessor.Response]()
    val detailProcessor = testKit.spawn(SudokuDetailProcessor[Block](id = 2))

    val update1 =
      Update(stringToReductionSet(Vector(
        "12345678 ",
        "1        ", // 1: Isolated & complete
        "   4     ", // 4: Isolated & complete
        "12 45678 ",
        "      78 ", // (7,8): Isolated & complete
        "       89",
        "      78 ", // (7,8): Isolated & complete
        "     6789",
        " 23   78 "
      )).zipWithIndex.map { _.swap},
        detailParentProbe.ref
      )

    detailProcessor ! update1

    val reducedUpdate1 = BlockUpdate(2, stringToReductionSet(Vector(
      " 23 56   ",
      "1        ",
      "   4     ",
      " 2  56   ",
      "      78 ",
      "        9",
      "      78 ",
      "     6  9",
      " 23      "
    )).zipWithIndex.map(_.swap)
    )

    detailParentProbe.expectMessage(reducedUpdate1)

    detailProcessor ! Update(cellUpdatesEmpty, detailParentProbe.ref)

    val reducedUpdate2 =
      BlockUpdate(2, stringToIndexedUpdate(
        Vector(
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "     6   ",
          ""
        ))
      )

    detailParentProbe.expectMessage(reducedUpdate2)

    detailProcessor ! Update(cellUpdatesEmpty, detailParentProbe.ref)

    val reducedUpdate3 =
      BlockUpdate(2, stringToIndexedUpdate(
        Vector(
          " 23 5    ",
          "",
          "",
          " 2  5    ",
          "",
          "",
          "",
          "",
          ""
        ))
      )

    detailParentProbe.expectMessage(reducedUpdate3)

    detailProcessor ! Update(cellUpdatesEmpty, detailParentProbe.ref)

    detailParentProbe.expectMessage(SudokuDetailUnchanged)

  } 
}
