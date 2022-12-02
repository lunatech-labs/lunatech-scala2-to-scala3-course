package org.lunatechlabs.dotty.sudoku

val cellIndexesVector: Vector[Int] = Vector.range(0, N)
val initialCell: Set[Int] = Set.range(1, 10)
val InitialDetailState = ReductionSet(cellIndexesVector.map(_ => initialCell))

opaque type ReductionSet = Vector[CellContent]

opaque type Sudoku = Vector[ReductionSet]


object ReductionSet:
  def apply(vs: Vector[Set[Int]]): ReductionSet =
    vs

object Sudoku:
  def apply(vrs: Vector[ReductionSet]): Sudoku =
    vrs

  def sudokuPrinter(sudoku: Sudoku): String =
    sudoku
      .sliding(3,3)
      .map(sudokuRowPrinter)
      .mkString("\n+---+---+---+\n", "+---+---+---+\n", "+---+---+---+")

extension (sudoku: Sudoku)
  def sliding(n1: Int, n2: Int): Iterator[Sudoku] =
    sudoku.sliding(n1, n2)

  def transpose: Sudoku = sudoku.transpose

  def reverse: Sudoku = sudoku.reverse

  def zipWithIndex = sudoku.zipWithIndex

  def rowSwap(row1: Int, row2: Int): Sudoku =
      sudoku.zipWithIndex.map {
        case (_, `row1`) => sudoku(row2)
        case (_, `row2`) => sudoku(row1)
        case (row, _) => row
      }
  
  def randomSwapAround: Sudoku =
    import scala.language.implicitConversions
    val possibleCellValues = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9)
    // Generate a random swapping of cell values. A value 0 is used as a marker for a cell
    // with an unknown value (i.e. it can still hold all values 0 through 9). As such
    // a cell with value 0 should remain 0 which is why we add an entry to the generated
    // Map to that effect
    val shuffledValuesMap =
    possibleCellValues.zip(scala.util.Random.shuffle(possibleCellValues)).to(Map) + (0 -> 0)
    sudoku.map { row =>
      row.shuffleValues(shuffledValuesMap)
    }

  def toRowUpdates: Vector[SudokuDetailProcessor.RowUpdate] =
    sudoku
      .map(_.zipWithIndex)
      .map(row => row.filterNot(_._1 == Set(0)))
      .zipWithIndex.filter(_._1.nonEmpty)
      .map { (c, i) =>
        SudokuDetailProcessor.RowUpdate(i, c.map(_.swap))
      }

// Extension instance wraps extension methods for type ReductionSet
extension (reductionSet: ReductionSet)
  // def zipWithIndex: Vector[(CellContent, Int)] =
  //   reductionSet.zipWithIndex

  def asInitialUpdates: Vector[(Int, CellContent)] =
    reductionSet.zipWithIndex.map(_.swap)

  def shuffleValues(shuffledValuesMap: Map[Int, Int]): ReductionSet =
    reductionSet.map(cell => Set(shuffledValuesMap(cell.head)))

  def mergeState(cellUpdates: CellUpdates): ReductionSet =
    cellUpdates.foldLeft(reductionSet) {
    case (stateTally, (index, updatedCellContent)) =>
      stateTally.updated(index, stateTally(index) & updatedCellContent)
  }

  def stateChanges(updatedState: ReductionSet): CellUpdates =
    (reductionSet zip updatedState).zipWithIndex.foldRight(cellUpdatesEmpty) {
      case (((previousCellContent, updatedCellContent), index), cellUpdates)
        if updatedCellContent != previousCellContent =>
        (index, updatedCellContent) +: cellUpdates

      case (_, cellUpdates) => cellUpdates
    }

  def isFullyReduced: Boolean =
    val allValuesInState = reductionSet.flatten
    allValuesInState == allValuesInState.distinct

  def applyReductionRuleOne: ReductionSet =
    val inputCellsGrouped = reductionSet.filter {_.size <= 7}.groupBy(identity)
    val completeInputCellGroups = inputCellsGrouped filter { (set, setOccurrences) =>
      set.size == setOccurrences.length
    }
    val completeAndIsolatedValueSets = completeInputCellGroups.keys.toList
    completeAndIsolatedValueSets.foldLeft(reductionSet) { (cells, caivSet) =>
      cells.map { cell =>
        if cell != caivSet then cell &~ caivSet else cell
      }
    }

  def applyReductionRuleTwo: ReductionSet =
    val valueOccurrences = CELLPossibleValues.map { value =>
      cellIndexesVector.zip(reductionSet).foldLeft(Vector.empty[Int]) {
        case (acc, (index, cell)) =>
          if cell contains value then index +: acc else acc
      }
    }

    val cellIndexesToValues =
      CELLPossibleValues
        .zip(valueOccurrences)
        .groupBy ((value, occurrence) => occurrence )
        .filter { case (loc, occ) => loc.length == occ.length && loc.length <= 6 }

    val cellIndexListToReducedValue = cellIndexesToValues.map { (index, seq) =>
      (index, (seq.map ((value, _) => value )).toSet)
    }

    val cellIndexToReducedValue = cellIndexListToReducedValue.flatMap { (cellIndexList, reducedValue) =>
      cellIndexList.map(cellIndex => cellIndex -> reducedValue)
    }

    reductionSet.zipWithIndex.foldRight(Vector.empty[CellContent]) {
      case ((cellValue, cellIndex), acc) =>
        cellIndexToReducedValue.getOrElse(cellIndex, cellValue) +: acc
    }

private def sudokuCellRepresentation(content: CellContent): String =
  content.toList match
    case Nil => "x"
    case singleValue +: Nil => singleValue.toString
    case _ => " "

private def sudokuRowPrinter(threeRows: Vector[ReductionSet]): String =
  val rowSubBlocks = for
    row <- threeRows
    rowSubBlock <- row.map(el => sudokuCellRepresentation(el)).sliding(3,3)
    rPres = rowSubBlock.mkString

  yield rPres
  rowSubBlocks.sliding(3,3).map(_.mkString("", "|", "")).mkString("|", "|\n|", "|\n")


extension (vrs: Vector[Set[Int]])
  def toReductionSet: ReductionSet = vrs

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

final case class SudokuField(sudoku: Sudoku)

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
      sudokuField.sudoku.rowSwap(row1, row2)
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
    SudokuField(sudokuField.sudoku.randomSwapAround)

