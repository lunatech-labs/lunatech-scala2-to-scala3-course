
Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `scala-3-snippets` = project
  .in(file("."))
  .settings(ThisBuild / scalaVersion := "3.2.2")
  .settings(
    name := "dotty-simple",
    version := "0.1.0",

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
  .aggregate(
    `new-control-structure-syntax`,
    `contextual-abstractions`,
    `intersection-and-union-types`,
    `enumerations`,
    `export-clause`,
    `top-level-definitions`,
  )

lazy val `new-control-structure-syntax` = project.in(file("new-control-structure-syntax"))

lazy val `contextual-abstractions` = project.in(file("contextual-abstractions"))

lazy val `enumerations` = project.in(file("enumerations"))

lazy val `export-clause` = project.in(file("export-clause"))

lazy val `intersection-and-union-types` = project.in(file("intersection-and-union-types"))

lazy val `top-level-definitions` = project.in(file("top-level-definitions"))

