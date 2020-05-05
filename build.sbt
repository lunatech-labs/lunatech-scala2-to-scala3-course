import sbt._

val dottyVersion = "0.24.0-RC1"

lazy val `scala-2-to-scala-3-master` = (project in file("."))
  .aggregate(
    common,
    `exercise_000_clustered_sudoku_solver_initial_state`,
    `exercise_002_top_level_definitions`,
    `exercise_003_parameter_untupling`,
    `exercise_004_extension_methods`,
    `exercise_005_using_and_summon`,
    `exercise_006_givens`,
    `exercise_007_enum_and_export`,
    `exercise_008_union_types`,
    `exercise_009_opaque_type_aliases`
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

lazy val `exercise_006_givens` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_007_enum_and_export` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_008_union_types` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_009_opaque_type_aliases` = project
  .settings(scalaVersion := dottyVersion)
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")
