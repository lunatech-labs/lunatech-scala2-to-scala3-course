package org.lunatechlabs.dotty.sudoku

opaque type ReductionSet = Vector[CellContent]

val InitialDetailState: ReductionSet = cellIndexesVector.map(_ => initialCell)

given CanEqual[ReductionSet, ReductionSet] = CanEqual.derived

object ReductionSet:
  def apply(vrs: Vector[CellContent]): ReductionSet = vrs

extension (reductionSet: ReductionSet)

  def applyReductionRuleOne: ReductionSet =
    val inputCellsGrouped = reductionSet.filter { _.size <= 7 }.groupBy(identity)
    val completeInputCellGroups = inputCellsGrouped.filter { (set, setOccurrences) =>
      set.size == setOccurrences.length
    }
    val completeAndIsolatedValueSets = completeInputCellGroups.keys.toList
    completeAndIsolatedValueSets.foldLeft(reductionSet) { (cells, caivSet) =>
      cells.map { cell =>
        if cell != caivSet then cell &~ caivSet else cell
      }
    }

  def applyReductionRuleTwo: ReductionSet =
    val valueOccurrences = CellPossibleValues.map { value =>
      cellIndexesVector.zip(reductionSet).foldLeft(Vector.empty[Int]) { case (acc, (index, cell)) =>
        if cell contains value then index +: acc else acc
      }
    }

    val cellIndexesToValues =
      CellPossibleValues.zip(valueOccurrences).groupBy((value, occurrence) => occurrence).filter { case (loc, occ) =>
        loc.length == occ.length && loc.length <= 6
      }

    val cellIndexListToReducedValue = cellIndexesToValues.map { (index, seq) =>
      (index, seq.map((value, _) => value).toSet)
    }

    val cellIndexToReducedValue = cellIndexListToReducedValue.flatMap { (cellIndexList, reducedValue) =>
      cellIndexList.map(cellIndex => cellIndex -> reducedValue)
    }

    reductionSet.zipWithIndex.foldRight(Vector.empty[CellContent]) { case ((cellValue, cellIndex), acc) =>
      cellIndexToReducedValue.getOrElse(cellIndex, cellValue) +: acc
    }

  def mergeState(cellUpdates: CellUpdates): ReductionSet =
    cellUpdates.foldLeft(reductionSet) { case (stateTally, (index, updatedCellContent)) =>
      stateTally.updated(index, stateTally(index) & updatedCellContent)
    }

  def stateChanges(updatedState: ReductionSet): CellUpdates =
    reductionSet
      .zip(updatedState)
      .zipWithIndex
      .foldRight(cellUpdatesEmpty):
        case (((previousCellContent, updatedCellContent), index), cellUpdates)
            if updatedCellContent != previousCellContent =>
          (index, updatedCellContent) +: cellUpdates

        case (_, cellUpdates) => cellUpdates

  def isFullyReduced: Boolean =
    val allValuesInState = reductionSet.flatten
    allValuesInState == allValuesInState.distinct

  def swapValues(shuffledValuesMap: Map[Int, Int]): ReductionSet =
    reductionSet.map(cell => Set(shuffledValuesMap(cell.head)))

  private def zipWithIndex: Vector[(CellContent, Int)] = reductionSet.zipWithIndex

end extension // ReductionSet

private def sudokuCellRepresentation(content: CellContent): String =
  content.toList match
    case Nil                => "x"
    case singleValue +: Nil => singleValue.toString
    case _                  => " "

extension (vrs: Vector[ReductionSet])
  def printSudokuRow: String =
    val rowSubBlocks = for
      row <- vrs
      rowSubBlock <- row.map(el => sudokuCellRepresentation(el)).sliding(3, 3)
      rPres = rowSubBlock.mkString
    yield rPres
    rowSubBlocks.sliding(3, 3).map(_.mkString("", "|", "")).mkString("|", "|\n|", "|\n")

// Collective Extensions:
// define extension methods that share the same left-hand parameter type under a single extension instance.
extension (sudokuField: SudokuField)

  def mirrorOnMainDiagonal: SudokuField = SudokuField(sudokuField.sudoku.transpose)

  def rotateCW: SudokuField = SudokuField(sudokuField.sudoku.reverse.transpose)

  def rotateCCW: SudokuField = SudokuField(sudokuField.sudoku.transpose.reverse)

  def flipVertically: SudokuField = SudokuField(sudokuField.sudoku.reverse)

  def flipHorizontally: SudokuField = sudokuField.rotateCW.flipVertically.rotateCCW

  def rowSwap(row1: Int, row2: Int): SudokuField =
    SudokuField(sudokuField.sudoku.zipWithIndex.map {
      case (_, `row1`) => sudokuField.sudoku(row2)
      case (_, `row2`) => sudokuField.sudoku(row1)
      case (row, _)    => row
    })

  def columnSwap(col1: Int, col2: Int): SudokuField =
    sudokuField.rotateCW.rowSwap(col1, col2).rotateCCW

  def randomSwapAround: SudokuField =
    import scala.language.implicitConversions
    val possibleCellValues = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9)
    // Generate a random swapping of cell values. A value 0 is used as a marker for a cell
    // with an unknown value (i.e. it can still hold all values 0 through 9). As such
    // a cell with value 0 should remain 0 which is why we add an entry to the generated
    // Map to that effect
    val shuffledValuesMap =
      possibleCellValues.zip(scala.util.Random.shuffle(possibleCellValues)).to(Map) + (0 -> 0)
    SudokuField(sudokuField.sudoku.map { row =>
      row.swapValues(shuffledValuesMap)
    })

  def toRowUpdates: Vector[SudokuDetailProcessor.RowUpdate] =
    sudokuField.sudoku
      .map(_.zipWithIndex)
      .map(row => row.filterNot(_._1 == Set(0)))
      .zipWithIndex
      .filter(_._1.nonEmpty)
      .map { (c, i) =>
        SudokuDetailProcessor.RowUpdate(i, c.map(_.swap))
      }
