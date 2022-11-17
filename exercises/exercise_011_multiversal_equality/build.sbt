Global / onChangedBuildSource := ReloadOnSourceChanges


lazy val `moving-from-scala-2-to-scala-3` =
  (project in file(".")).settings(
    scalaVersion := "3.1.2",
    Compile / scalacOptions ++= CompileOptions.compileOptions,
    libraryDependencies ++= Dependencies.dependencies,
    libraryDependencies ++= Dependencies.crossDependencies.map(_.cross(CrossVersion.for3Use2_13)),
    testFrameworks += new TestFramework("munit.Framework"),
  )

sbt.addCommandAlias("runSolver", "runMain org.lunatechlabs.dotty.SudokuSolverMain")
