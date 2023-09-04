Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `moving-from-scala-2-to-scala-3` =
  (project in file(".")).settings(
    scalaVersion := "3.3.0",
    Compile / scalacOptions ++= CompileOptions.compileOptions,
    libraryDependencies ++= Dependencies.dependencies,
    testFrameworks += new TestFramework("munit.Framework"))

sbt.addCommandAlias("runSolver", "runMain org.lunatechlabs.dotty.SudokuSolverMain")
