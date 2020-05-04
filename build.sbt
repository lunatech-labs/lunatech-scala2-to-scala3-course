import sbt._

val dottyVersion = "0.23.0-RC1"

lazy val `scala-2-to-scala-3-master` = (project in file("."))
  .aggregate(
    common,
    `exercise_000_clustered_sudoku_solver_initial_state`,
    `exercise_002_top_level_definitions`,
    `exercise_003_parameter_untupling`,
    `exercise_004_extension_methods`,
    `exercise_005_using_and_summon`
 )
  .settings(scalaVersion := dottyVersion)
 .settings(CommonSettings.commonSettings: _*)

lazy val common = project
  .settings(scalaVersion := dottyVersion)
  .settings(CommonSettings.commonSettings: _*)

lazy val `exercise_000_clustered_sudoku_solver_initial_state` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_002_top_level_definitions` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_003_parameter_untupling` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_004_extension_methods` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_005_using_and_summon` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")
       