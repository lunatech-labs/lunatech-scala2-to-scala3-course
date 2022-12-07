# Opaque Type Aliases

## Background

An Opaque Type Alias can be used to provide the functionality of a "wrapper
type" (i.e. a type that wraps, and therefore hides, another type) but without
any runtime overhead. The aim is to provide additional type-safety at
compile-time but then be stripped away at runtime. It is a powerful new feature
of Scala 3 for supporting Information Hiding.

Opaque Type Aliases differ from plain Scala 2 Type Aliases in that the later
just provide a new name for a type but wherever this new name is used, the
call-site still knows the details of the original type being aliased. With
Opaque Type Aliases, the original type being aliased is hidden (or is opaque) at
the call-site.

## Steps

We will change two type aliases, `ReductionSet` and `Sudoku` into opaque type
aliases and we will do this in two steps tackling the conversion of
`ReductionSet` first. Each time you change the code, recompile the project to see
the effect. Don't forget to compile the tests too...

> __Tip:__ Fix all of the compilation errors of this form `value {name} is not a
      member of ...` before tackling the other types of error like `Found: ...
      Required: ...`

> __Tip:__ Some of the missing members for our new opaque type are generic (i.e.
      type-parameterised) methods. Do not be afraid to implement extension
      methods that are non-generic (i.e. without type-parameters), which might
      mean modifying existing call-sites.

- Have a look at the `TopLevelDefinitions.scala` file. This is the current definition
  of the `ReductionSet` type alias (the definition of `CellContent` is shown too for
  clarity; we will leave this type alias unmodified):

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
