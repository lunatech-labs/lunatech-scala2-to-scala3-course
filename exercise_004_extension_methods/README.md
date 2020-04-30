# Extension Methods

## Background

Extension methods can be used to add methods to types after they are defined.

## Steps

```scala
extension sudokuFieldOps on (sudokuField: SudokuField) {
  def transpose: SudokuField = SudokuField(sudokuField.sudoku.transpose)
}
```