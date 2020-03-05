
lazy val `pi_cluster_master` = (project in file("."))
  .aggregate(
    common,
    `exercise_012_clustered_sudoku_solver`,
 ).settings(CommonSettings.commonSettings: _*)

lazy val common = project.settings(CommonSettings.commonSettings: _*)

lazy val `exercise_012_clustered_sudoku_solver` = project
  .configure(CommonSettings.configure)
  .dependsOn(common % "test->test;compile->compile")
       
