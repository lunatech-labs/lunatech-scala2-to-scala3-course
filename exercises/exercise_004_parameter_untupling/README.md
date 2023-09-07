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

Imagine you want to map `pairs` to a list of `Int`s so that each pair of numbers is 
mapped to their sum. The best way to do this in _Scala 2.x_ was with a pattern 
matching anonymous function.

```scala
pairs map {
  case (x, y) => x + y
}
// res1: List[Int] = List(1, 3, 5)
```
Scala 3 now allows us to write

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

- We can apply the new way of encoding in several places in the source code.

- Have a look for them in the following files:
  - `ReductionRules.scala`
  - `SudokuProblemSender.scala`
  - `SudokuProgressTracker.scala`
  - `SudokuSolver.scala`
  - `TopLevelDefinitions.scala` (we chose to put the content of the package
                               object in a file with this name.)

- Identify them and replace the pattern-matching syntax with the new syntax

- Run the provided tests by executing the `test` command from the `sbt` prompt
  and verify that all tests pass

- Verify that the application runs correctly

### Next steps

After successfully completing the tasks in this exercise, move to the next one by
running the `cmtc next-exercise` from the command line.

## Source code formatting & Markdown viewer in IntelliJ

### Source code formatting

[scalafmt](https://github.com/scalameta/scalafmt) based source code formatting is
in place in this project. scalafmt supports both Scala 2 and Scala 3. You can
[re]format the code by running `scalafmtAll` from the sbt prompt. As we switch from
Scala 2 to Scala 3, you need to make sure that a matching scalafmt configuration is
in place. In any of the exercises, you can run `cmtc pull-template .scalafmt.conf`
to "pull-in" the correct configuration file.

### Markdown viewer in IntelliJ

The font size can be a bit too small for the taste of some people. You can change the
Markdown zoom setting in IntelliJ by pasting the following CSS snippet in the
markdown setting in _" Settings" -> "Languages & Frameworks" -> "Custom CSS -> CSS rules"_
and adjust the font-size setting to your liking:

```
body {
  font-size: 120% !important;
  }
```

![IntelliJ Markdown viewer settings](images/Markdown-viewer-IntelliJ.png)
