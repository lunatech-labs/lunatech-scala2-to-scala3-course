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

- Open the `TopLevelDefinitions.scala` file that you created during the exercise
  on Top-Level Definitions. Here you should see a few type aliases that were
  created to help with the readability of the code.

```scala
...
type CellContent = Set[Int]
type ReductionSet = Vector[CellContent]
type Sudoku = Vector[ReductionSet]

type CellUpdates = Vector[(Int, Set[Int])]
...
```

To keep things manageable we will only focus on one of these type aliases for
this exercise. Specifically we will convert the last of these type aliases,
`ReductionSet` into an Opaque Type Alias.

- Start by moving the type alias to a new source file, `ReductionSet`.

- Now add the keyword `opaque` in front of the type alias
  declaration and recompile. Do you expect this to compile successfully? If not,
  why?

- So it seems we need to tackle quite a few compilation errors... Let's get
  started.

> Tip: In general, try to fix all of the compilation errors of this form
>      `value {name} is not a member of ...` before tackling the other types
>      of error like `Found: ... Required: ...`

- The first errors are in the source file `ReductionRules`. As this file contains
  two extension methods on `ReductionSet`, moving these to `ReductionSet` will
  resolve these errors.

- Next up is an error linked to value `InitialDetailState`. It's an
  `InitialDetailState` which is a `ReductionSet`. Again, move it to the
   `ReductionSet` source file. You may have to annotate its type.

- Recompile and look at the first few error in source file `SudokuDetailProcessor`.
  These are linked to methods `mergeState`, `stateChanges`, and `isFullyReduced`.
  As these 3 methods all take a `ReductionSet` as argument, it makes sense to
  convert them to extension methods on `ReductionSet`. Do so, and adapt the
  call sites to take the change into account. Note that you may have to change
  the visibility of the methods.

- Recompile and notice the errors in `SudokuIO`. The relevant code is doing some
  kind of pretty print of a `Vector[ReductionSet]` (which is a `Sudoku`; see the
  type alias in `TopLevelDefinitions`). Move the method `SudokuRowPrinter` to
  `ReductionSet` and convert it to an extension method (name it `printSudokuRow`).
  Move the private `sudokuCellRepresentation` helper method along with it. Make
  the necessary changes at the call site.

- Recompile. Notice the extension methods on `SudokuField` that are now in error.
  Move these to `ReductionSet` and recompile. Methods `randomSwapAround` and
  `toRowUpdates` show errors.

- The core issue with method `randomSwapAround` is the mapping on a `ReductionSet`.
  One way to tackle this is to add an extension method on `ReductionSet` with the
  following signature: `def swapValues(shuffledValuesMap: Map[Int, Int]): ReductionSet`
  Make the necessary changes to `randomSwapAround` to make it compile.

- The core issue with method `toRowUpdates` is invoking `zipWithIndex` on `ReductionSet`.
  Add a private extension method `zipWithIndex` on `ReductionSet`.

- We've almost eliminated all complilation errors. The last error can be corrected
  by adding an apply method in a `ReductionSet` companion object that allows
  one to create a `ReductionSet` from a `Vector[CellContent]`.

- Once all the compilation errors are fixed, run the provided tests by executing
  the `test` command from the `sbt` prompt and verify that all tests pass

- Verify that the application runs correctly

## Conclusions

In summary, all we did was changing a single type alias into an opaque one.

It does raise some questions:

- Was it worth the effort?
  - What have we gained, if anything?
- Are there alternatives to this approach?
- What are the gotchas 

There are many more potential type aliases to which we could apply the same
procedure. One that sticks out is the `Sudoku` type alias
(`type Sudoku = Vector[ReductionSet`).





