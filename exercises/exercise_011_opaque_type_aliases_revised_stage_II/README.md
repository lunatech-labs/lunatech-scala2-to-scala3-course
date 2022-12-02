# Opaque Type Aliases

## Steps

In this step you will convert the `Sudoku` type alias into an opaque type alias.

In file `TopLevelDefinitions.scala`, we have the following:

```scala
type Sudoku = Vector[ReductionSet]
```

- Move `Sudoku` type alias to source file (`ReductionSet.scala`).
- Convert it to an opaque type alias.`
- Compile again which shows compilation errors.

> Tip: while moving to this exercise, a change was applied to the tests. Have a look
  at calls to a method named `asInitialUpdates`. What type of method is this?
  What is its signature?

- Use the same methodology as applied in the previous exercise to fix the
  compilation errors. Think about using extension methods and apply method(s)
  on the `Sudoku` type.

- Once all the compilation errors are fixed, run the provided tests by executing
  the `test` command from the `sbt` prompt and verify that all tests pass.

- Verify that the application runs correctly.
