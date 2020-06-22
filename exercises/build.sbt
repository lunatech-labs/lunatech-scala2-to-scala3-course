/***************************************************************
  *      THIS IS A GENERATED FILE - EDIT AT YOUR OWN RISK      *
  **************************************************************
  *
  * Use the mainadm command to generate a new version of
  * this build file.
  *
  * See https://github.com/lightbend/course-management-tools
  * for more details
  *
  */

import sbt._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `scala-2-to-scala-3-main` = (project in file("."))
  .aggregate(
    common,
    `exercise_000_sudoku_solver_initial_state`,
    `exercise_001_dotty_deprecated_syntax_rewriting`,
    `exercise_002_dotty_new_syntax_and_indentation_based_syntax`,
    `exercise_003_top_level_definitions`,
    `exercise_004_parameter_untupling`,
    `exercise_005_extension_methods`,
    `exercise_006_using_and_summon`,
    `exercise_007_givens`,
    `exercise_008_enum_and_export`,
    `exercise_009_union_types`,
    `exercise_010_opaque_type_aliases`,
    `exercise_011_multiversal_equality`
  )
  .settings(ThisBuild / scalaVersion := Version.scalaVersion)
  .settings(CommonSettings.commonSettings: _*)

lazy val common = project
  .settings(CommonSettings.commonSettings: _*)

lazy val `exercise_000_sudoku_solver_initial_state` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_001_dotty_deprecated_syntax_rewriting` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_002_dotty_new_syntax_and_indentation_based_syntax` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_003_top_level_definitions` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_004_parameter_untupling` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_005_extension_methods` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_006_using_and_summon` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_007_givens` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_008_enum_and_export` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_009_union_types` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_010_opaque_type_aliases` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")

lazy val `exercise_011_multiversal_equality` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")
       