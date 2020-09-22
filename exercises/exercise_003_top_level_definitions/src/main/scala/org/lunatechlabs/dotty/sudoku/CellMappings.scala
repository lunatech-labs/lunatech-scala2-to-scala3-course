package org.lunatechlabs.dotty.sudoku

object CellMappings:

  def rowToColumnCoordinates(rowNr: Int, cellNr: Int): (Int, Int) =
    (cellNr, rowNr)

  def rowToBlockCoordinates(rowNr: Int, cellNr: Int): (Int, Int) =
    ((rowNr / 3) * 3 + cellNr / 3, (rowNr % 3) * 3 + cellNr % 3)

  def columnToRowCoordinates(columnNr: Int, cellNr: Int): (Int, Int) =
    (cellNr, columnNr)

  def columnToBlockCoordinates(columnNr: Int, cellNr: Int): (Int, Int) =
    rowToBlockCoordinates(cellNr, columnNr)

  def blockToRowCoordinates(blockNr: Int, cellNr: Int): (Int, Int) =
    ((blockNr / 3) * 3 + cellNr / 3, (blockNr % 3) * 3 + cellNr % 3)

  def blockToColumnCoordinates(blockNr: Int, cellNr: Int): (Int, Int) =
    ((blockNr % 3) * 3 + cellNr % 3, (blockNr / 3) * 3 + cellNr / 3)
