package org.lunatechlabs.dotty.sudoku

import org.lunatechlabs.dotty.sudoku.CellMappings._

class CellMappingSuite extends munit.FunSuite:

  test("Mapping row coordinates should result in correct column & block coordinates") {
    assertEquals(rowToColumnCoordinates(0, 0), ((0, 0)))
    assertEquals(rowToBlockCoordinates(0, 0), ((0, 0)))
    assertEquals(rowToColumnCoordinates(8, 8), ((8, 8)))
    assertEquals(rowToBlockCoordinates(8, 8), ((8, 8)))
    assertEquals(rowToColumnCoordinates(3, 4), ((4, 3)))
    assertEquals(rowToBlockCoordinates(3, 4), ((4, 1)))
    assertEquals(rowToBlockCoordinates(4, 3), ((4, 3)))
  }

  test("Mapping column coordinates should result in correct row & block coordinates") {
    assertEquals(columnToRowCoordinates(0, 0), ((0, 0)))
    assertEquals(columnToBlockCoordinates(0, 0), ((0, 0)))
    assertEquals(columnToRowCoordinates(8, 8), ((8, 8)))
    assertEquals(columnToBlockCoordinates(8, 8), ((8, 8)))
    assertEquals(columnToRowCoordinates(3, 4), ((4, 3)))
    assertEquals(columnToBlockCoordinates(3, 4), ((4, 3)))
    assertEquals(columnToBlockCoordinates(4, 3), ((4, 1)))
  }

  test("Mapping block coordinates should result in correct row & column coordinates") {
    assertEquals(blockToRowCoordinates(0, 0), ((0, 0)))
    assertEquals(blockToColumnCoordinates(0, 0), ((0, 0)))
    assertEquals(blockToRowCoordinates(8, 8), ((8, 8)))
    assertEquals(blockToColumnCoordinates(8, 8), ((8, 8)))
    assertEquals(blockToRowCoordinates(4, 3), ((4, 3)))
    assertEquals(blockToColumnCoordinates(4, 3), ((3, 4)))
    assertEquals(blockToRowCoordinates(3, 4), ((4, 1)))
    assertEquals(blockToColumnCoordinates(3, 4), ((1, 4)))
    assertEquals(blockToRowCoordinates(5, 5), ((4, 8)))
    assertEquals(blockToColumnCoordinates(5, 5), ((8, 4)))
  }
