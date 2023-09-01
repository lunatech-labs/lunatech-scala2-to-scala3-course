Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `lunatech-scala2-to-scala3-course` =
  (project in file(".")).settings(
    scalaVersion := "2.13.10",
    Compile / scalacOptions ++= CompileOptions.compileOptions,
    libraryDependencies ++= Dependencies.dependencies,
    testFrameworks += new TestFramework("munit.Framework"))

run / fork := true
run / connectInput := true

sbt.addCommandAlias("runSolver", "runMain org.lunatechlabs.dotty.SudokuSolverMain")
