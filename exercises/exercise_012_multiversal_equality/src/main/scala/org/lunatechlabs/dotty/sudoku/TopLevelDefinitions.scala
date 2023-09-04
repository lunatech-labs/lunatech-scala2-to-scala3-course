package org.lunatechlabs.dotty.sudoku

private val N = 9
val CellPossibleValues: Vector[Int] = (1 to N).toVector
val cellIndexesVector: Vector[Int] = Vector.range(0, N)
val initialCell: Set[Int] = Set.range(1, 10)

type CellContent = Set[Int]
type Sudoku = Vector[ReductionSet]

type CellUpdates = Vector[(Int, CellContent)]
val cellUpdatesEmpty = Vector.empty[(Int, Set[Int])]

final case class SudokuField(sudoku: Sudoku)

import SudokuDetailProcessor.RowUpdate

extension (update: Vector[SudokuDetailProcessor.RowUpdate])
  def toSudokuField: SudokuField =
    import scala.language.implicitConversions
    val rows =
      update
        .map { case SudokuDetailProcessor.RowUpdate(id, cellUpdates) => (id, cellUpdates) }
        .to(Map)
        .withDefaultValue(cellUpdatesEmpty)
    val sudoku = for
      (row, cellUpdates) <- Vector.range(0, 9).map(row => (row, rows(row)))
      x = cellUpdates.to(Map).withDefaultValue(Set(0))
      y = ReductionSet(Vector.range(0, 9).map(n => x(n)))
    yield y
    SudokuField(sudoku)
