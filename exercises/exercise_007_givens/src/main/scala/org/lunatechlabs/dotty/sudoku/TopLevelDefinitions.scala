package org.lunatechlabs.dotty.sudoku

private val N = 9
val CELLPossibleValues: Vector[Int] = (1 to N).toVector
val cellIndexesVector: Vector[Int] = Vector.range(0, N)
val initialCell: Set[Int] = Set.range(1, 10)

type CellContent = Set[Int]
type ReductionSet = Vector[CellContent]
type Sudoku = Vector[ReductionSet]

type CellUpdates = Vector[(Int, Set[Int])]
val cellUpdatesEmpty = Vector.empty[(Int, Set[Int])]

import SudokuDetailProcessor.RowUpdate

extension (update: Vector[SudokuDetailProcessor.RowUpdate]) def toSudokuField: SudokuField =
  import scala.language.implicitConversions
  val rows =
        update
          .map { case SudokuDetailProcessor.RowUpdate(id, cellUpdates) => (id, cellUpdates)}
          .to(Map).withDefaultValue(cellUpdatesEmpty)
  val sudoku = for
    (row, cellUpdates) <- Vector.range(0, 9).map(row => (row, rows(row)))
    x = cellUpdates.to(Map).withDefaultValue(Set(0))
    y = Vector.range(0, 9).map(n => x(n))
  yield y
  SudokuField(sudoku)

// Collective Extensions:
// define extension methods that share the same left-hand parameter type under a single extension instance.
extension (sudokuField: SudokuField)

  def transpose: SudokuField = SudokuField(sudokuField.sudoku.transpose)

  def rotateCW: SudokuField = SudokuField(sudokuField.sudoku.reverse.transpose)

  def rotateCCW: SudokuField = SudokuField(sudokuField.sudoku.transpose.reverse)

  def flipVertically: SudokuField = SudokuField(sudokuField.sudoku.reverse)

  def flipHorizontally: SudokuField = sudokuField.rotateCW.flipVertically.rotateCCW

  def rowSwap(row1: Int, row2: Int): SudokuField =
    SudokuField(
          sudokuField.sudoku.zipWithIndex.map {
          case (_, `row1`) => sudokuField.sudoku(row2)
          case (_, `row2`) => sudokuField.sudoku(row1)
          case (row, _) => row
          }
        )

  def columnSwap(col1: Int, col2: Int): SudokuField =
    sudokuField.rotateCW.rowSwap(col1, col2).rotateCCW

  def randomSwapAround: SudokuField =
    import scala.language.implicitConversions
    val possibleCellValues = Vector(1,2,3,4,5,6,7,8,9)
    // Generate a random swapping of cell values. A value 0 is used as a marker for a cell
    // with an unknown value (i.e. it can still hold all values 0 through 9). As such
    // a cell with value 0 should remain 0 which is why we add an entry to the generated
    // Map to that effect
    val shuffledValuesMap =
      possibleCellValues.zip(scala.util.Random.shuffle(possibleCellValues)).to(Map) + (0 -> 0)
    SudokuField(sudokuField.sudoku.map { row =>
      row.map(cell => Set(shuffledValuesMap(cell.head)))
    })

  def toRowUpdates: Vector[SudokuDetailProcessor.RowUpdate] =
    sudokuField
      .sudoku
      .map(_.zipWithIndex)
      .map(row => row.filterNot(_._1 == Set(0)))
      .zipWithIndex.filter(_._1.nonEmpty)
      .map { (c, i) =>
        SudokuDetailProcessor.RowUpdate(i, c.map(_.swap))
      }
