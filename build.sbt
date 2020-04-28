
val dottyVersion = "0.23.0-RC1"
val selectedScalaVersion = dottyVersion
//val selectedScalaVersion = dottyLatestNightlyBuild.get

lazy val `scala-2-to-scala-3-master` = (project in file("."))
  .aggregate(
    common,
    `exercise_000_clustered_sudoku_solver_initial_state`,
    `exercise_002_top_level_definitions`,
  ).settings(
    scalaVersion := selectedScalaVersion,
    organization := "org.lunatech.scala",
    version := "1.3.0",
  )
  .settings(CommonSettings.commonSettings: _*)

lazy val common = project
  .settings(scalaVersion := selectedScalaVersion)
  .settings(CommonSettings.commonSettings: _*)

lazy val `exercise_000_clustered_sudoku_solver_initial_state` = project
  .settings(scalaVersion := selectedScalaVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_002_top_level_definitions` = project
  .settings(scalaVersion := selectedScalaVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

