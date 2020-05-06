# Parameter untupling

## Background

The need to write a pattern-matching decomposition when mapping over a sequence 
of tuples has always annoyed some Scala developers. So many might appreciate this 
new feature.

Consider you have a list of pairs
```scala
val pairs = List(1, 2, 3).zipWithIndex
// pairs: List[(Int, Int)] = List((1,0), (2,1), (3,2))
```

If you want to map `pairs` to a list of `Int`s so that each pair of numbers is 
mapped to their sum. The best way to do this in _Scala 2.x_ was with a pattern 
matching anonymous function.

```scala
pairs map {
  case (x, y) => x + y
}
// res1: List[Int] = List(1, 3, 5)
```
Dotty now allows us to write

```scala
pairs map {
  (x, y) => x + y
}
// val res0: List[Int] = List(1, 3, 5)
```
or 
```scala
pairs.map(_ + _)
// val res1: List[Int] = List(1, 3, 5)
```

## Steps

- We have similar usage in the following files
  - `ReductionRules.scala`
  - `SudokuProblemSender.scala`
  - `SudokuProgressTracker.scala`
  - `SudokuSolver.scala`
  - `TopLevelDefinitions.scala`
- Identify them and replace the pattern-matching syntax with the new syntax.
- Verify that the application compiles and run correctly.