package org.lunatechlabs.dotty.sudoku

import akkapi.cluster.sudoku.CellMappings._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellMappingSpec extends AnyWordSpec with Matchers {

  "Mapping row coordinates" should {
    "result in correct column & block coordinates" in {
      rowToColumnCoordinates(0, 0) shouldBe ((0, 0))
      rowToBlockCoordinates(0, 0)  shouldBe ((0, 0))
      rowToColumnCoordinates(8, 8) shouldBe ((8, 8))
      rowToBlockCoordinates(8, 8)  shouldBe ((8, 8))
      rowToColumnCoordinates(3, 4) shouldBe ((4, 3))
      rowToBlockCoordinates(3, 4)  shouldBe ((4, 1))
      rowToBlockCoordinates(4, 3)  shouldBe ((4, 3))
    }
  }

  "Mapping column coordinates" should {
    "result in correct row & block coordinates" in {
      columnToRowCoordinates(0, 0)   shouldBe ((0, 0))
      columnToBlockCoordinates(0, 0) shouldBe ((0, 0))
      columnToRowCoordinates(8, 8)   shouldBe ((8, 8))
      columnToBlockCoordinates(8, 8) shouldBe ((8, 8))
      columnToRowCoordinates(3, 4)   shouldBe ((4, 3))
      columnToBlockCoordinates(3, 4) shouldBe ((4, 3))
      columnToBlockCoordinates(4, 3) shouldBe ((4, 1))
    }
  }

  "Mapping block coordinates" should {
    "result in correct row & column coordinates" in {
      blockToRowCoordinates(0, 0)    shouldBe ((0, 0))
      blockToColumnCoordinates(0, 0) shouldBe ((0, 0))
      blockToRowCoordinates(8, 8)    shouldBe ((8, 8))
      blockToColumnCoordinates(8, 8) shouldBe ((8, 8))
      blockToRowCoordinates(4, 3)    shouldBe ((4, 3))
      blockToColumnCoordinates(4, 3) shouldBe ((3, 4))
      blockToRowCoordinates(3, 4)    shouldBe ((4, 1))
      blockToColumnCoordinates(3, 4) shouldBe ((1, 4))
      blockToRowCoordinates(5, 5)    shouldBe ((4, 8))
      blockToColumnCoordinates(5, 5) shouldBe ((8, 4))
    }
  }
}
