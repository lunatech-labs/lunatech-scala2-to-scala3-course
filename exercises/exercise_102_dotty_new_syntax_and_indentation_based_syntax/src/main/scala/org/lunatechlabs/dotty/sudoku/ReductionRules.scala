package org.lunatechlabs.dotty.sudoku

object ReductionRules:

  def reductionRuleOne(reductionSet: ReductionSet): ReductionSet =
    val inputCellsGrouped = reductionSet.filter(_.size <= 7).groupBy(identity)
    val completeInputCellGroups = inputCellsGrouped.filter {
      case (set, setOccurrences) => set.size == setOccurrences.length
    }
    val completeAndIsolatedValueSets = completeInputCellGroups.keys.toList
    completeAndIsolatedValueSets.foldLeft(reductionSet) {
      case (cells, caivSet) =>
        cells.map { cell =>
          if cell != caivSet then cell &~ caivSet else cell
        }
    }

  def reductionRuleTwo(reductionSet: ReductionSet): ReductionSet =
    val valueOccurrences = CELLPossibleValues.map { value =>
      cellIndexesVector.zip(reductionSet).foldLeft(Vector.empty[Int]) {
        case (acc, (index, cell)) =>
          if cell contains value then index +: acc else acc
      }
    }

    val cellIndexesToValues =
      CELLPossibleValues
        .zip(valueOccurrences)
        .groupBy { case (value, occurrence) => occurrence }
        .filter { case (loc, occ) => loc.length == occ.length && loc.length <= 6 }

    val cellIndexListToReducedValue = cellIndexesToValues.map {
      case (index, seq) => (index, (seq.map { case (value, _) => value }).toSet)
    }

    val cellIndexToReducedValue = cellIndexListToReducedValue.flatMap {
      case (cellIndexList, reducedValue) =>
        cellIndexList.map(cellIndex => cellIndex -> reducedValue)
    }

    reductionSet.zipWithIndex.foldRight(Vector.empty[CellContent]) {
      case ((cellValue, cellIndex), acc) =>
        cellIndexToReducedValue.getOrElse(cellIndex, cellValue) +: acc
    }
