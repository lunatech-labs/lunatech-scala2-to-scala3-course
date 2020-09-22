package org.lunatechlabs.dotty.sudoku

trait SudokuTestHelpers:

  def stringToReductionSet(stringDef: Vector[String]): ReductionSet =
    for {
      cellString <- stringDef
    } yield cellString.replaceAll(" ", "").map { _.toString.toInt }.toSet

  def stringToIndexedUpdate(stringDef: Vector[String]): CellUpdates =
    val updates = for {
      (cellString, index) <- stringDef.zipWithIndex if cellString != ""
    } yield (index, cellString.replaceAll(" ", "").map { _.toString.toInt }.toSet)
    CellUpdates(updates:_ *)

  def applyReductionRules(reductionSet: ReductionSet): ReductionSet = reductionSet.applyReductionRuleOne.applyReductionRuleTwo

