Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `moving-from-scala-2-to-scala-3` =
  (project in file(".")).settings(
    scalaVersion := "2.13.10",
    Compile / scalacOptions ++= CompileOptions.compileOptions,
    libraryDependencies ++= Dependencies.dependencies,
    testFrameworks += new TestFramework("munit.Framework"))

run / fork := true
run / connectInput := true

sbt.addCommandAlias("runSolver", "runMain org.lunatechlabs.dotty.SudokuSolverMain")
