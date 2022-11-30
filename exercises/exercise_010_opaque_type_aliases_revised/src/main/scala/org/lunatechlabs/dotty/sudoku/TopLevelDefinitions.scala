package org.lunatechlabs.dotty.sudoku

private val N = 9
val CELLPossibleValues: Vector[Int] = (1 to N).toVector

type CellContent = Set[Int]
type Sudoku = Vector[ReductionSet]

type CellUpdates = Vector[(Int, Set[Int])]
val cellUpdatesEmpty = Vector.empty[(Int, Set[Int])]
