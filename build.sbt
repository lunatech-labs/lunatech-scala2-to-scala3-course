
val dottyVersion = "0.23.0-RC1"
val selectedScalaVersion = dottyVersion
//val selectedScalaVersion = dottyLatestNightlyBuild.get

lazy val `pi_cluster_master` = (project in file("."))
  .aggregate(
    common,
    `exercise_012_clustered_sudoku_solver`,
  ).settings(
    scalaVersion := selectedScalaVersion,
    organization := "org.lunatech.scala",
    version := "1.3.0",
  )
  .settings(CommonSettings.commonSettings: _*)

lazy val common = project
  .settings(scalaVersion := selectedScalaVersion)
  .settings(CommonSettings.commonSettings: _*)

lazy val `exercise_012_clustered_sudoku_solver` = project
  .settings(scalaVersion := selectedScalaVersion)
  .settings(maintainer := "eric.loots@gmail.com")
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")
       
