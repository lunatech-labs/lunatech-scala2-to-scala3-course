# Opaque Type Aliases

## Steps

In this step you will convert the `Sudoku` type alias into an opaque type alias.

In file `TopLevelDefinitions.scala`, we have the following:

```scala
type CellContent = Set[Int]
type ReductionSet = Vector[CellContent]
```

- Move `ReductionSet` type alias to its own source file (`ReductionSet.scala`).
- Convert it to an opaque type alias.`
- Compile again which shows compilation errors in file `ReductionRules.scala`;
  move the extension methods to `ReductionSet.scala` as they are extensions on
  `ReductionSet`.
- As `InitialDetailState` is a `ReductionSet`, move it to `ReductionSet.scala`.
  Move `cellIndexesVector` and `initialCell` with it.
- Create an apply method in a `ReductionSet` companion object that allows us
  to initialise `InitialDetailState` via a call to `ReductionSet(cellIndexesVector)`.

- Compile again.
- Next up is `stateTally.updated` in `SudokuDetailProcessor.scala`. As it operates
  on a state of type `ReductionSet` and returns a `ReductionSet`, it makes sense
  to move this to `ReductionSet.scala` and to convert it to an extension method.
- Repeat this for `stateChanges` and `isFullyReduced` in `SudokuDetailProcessor.scala`.

- Compile again.
- Next errors reported are in `SudokuIO.scala`. Although the move is less clear cut,
  we move methods `sudokuCellRepresentation`, `sudokuRowPrinter`, and `sudokuPrinter`
  to `ReductionSet.scala`. We may need to revisit this move later.
- Change `sudokuPrinter` to take a `Sudoku` as argument. Change this at the (only)
  call site of this method.

- Compile again.
- You will see that compilation errors are reported in the `TopLevelDefinitions.scala`
  source file. These are linked to the extension methods defined on `SudokuField`.
- Move these extension methods to `ReductionSet.scala`.
- You will see that compilation errors remain in the `randomSwapAround` and
  `toRowUpdates` extension methods. Fix these errors. Defining two extra extension
  methods on `ReductionSet` and using these at the right spot should be sufficient.

- Once all the compilation errors are fixed, run the provided tests by executing
  the `test` command from the `sbt` prompt and verify that all tests pass

- Verify that the application runs correctly
