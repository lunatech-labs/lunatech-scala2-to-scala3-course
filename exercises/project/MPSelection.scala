package sbtstudent

/**
  * Copyright © 2016-2020 Lightbend, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *
  * NO COMMERCIAL SUPPORT OR ANY OTHER FORM OF SUPPORT IS OFFERED ON
  * THIS SOFTWARE BY LIGHTBEND, Inc.
  *
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import sbt._
import sbt.{IO => sbtio}
import sbt.complete.DefaultParsers.{IntBasic, OptNotSpace, OptSpace, Space}

import scala.util.matching.Regex

object MPSelection {

  val ExercisePathSpec: Regex = """(.*_)(\d{3})(_\w+)$""".r

  object FoldersOnly {
    def apply() = new FoldersOnly
  }
  class FoldersOnly extends java.io.FileFilter {
    override def accept(f: File): Boolean = f.isDirectory
  }

  private lazy val ExerciseNr = Space ~> IntBasic.?

  def setActiveExerciseNr: Command = Command("setActiveExerciseNr")(s => ExerciseNr) { (state: State, arg: Option[Int]) =>
    arg match {
      case Some(exNr) =>
        activateExerciseNr(state, exNr)
      case _ => state
    }
  }

  def activateAllExercises: Command = Command.command("activateAllExercises") { state =>
    val selectedProjectFolder = new sbt.File(Project.extract(state).structure.root)
    val exercises = getExerciseNames(selectedProjectFolder)
    genBuildFile(selectedProjectFolder, exercises)
    "reload" :: state
  }

  def activateExerciseNr(state: State, nr: Int): State = {
    val selectedProjectFolder = new sbt.File(Project.extract(state).structure.root)
    val exercises = getExerciseNames(selectedProjectFolder)
    val activeExercisePrefix = f".*_$nr%03d.*"
    val activeExercisePrefixSpec = activeExercisePrefix.r
    val activeExercise = exercises.find(exerciseName => activeExercisePrefixSpec.findFirstIn(exerciseName).isDefined)
    if (activeExercise.isEmpty) {
      println(s"No exercise found with nr = $nr")
      state
    } else {
      genBuildFile(selectedProjectFolder, Vector(activeExercise.get))
      "reload" :: state
    }
  }

  def exerciseProjects(exercises: Vector[String], multiJVM: Boolean): String = {
    multiJVM match {
      case (false) =>
        exercises.map {exercise =>
          s"""lazy val $exercise = project
             |  .settings(CommonSettings.commonSettings: _*)
             |  .dependsOn(common % "test->test;compile->compile")
             |""".stripMargin
        }.mkString("", "\n", "\n")
      case (true) =>
        exercises.map {exercise =>
          s"""lazy val $exercise = project
             |  .settings(SbtMultiJvm.multiJvmSettings: _*)
             |  .settings(CommonSettings.commonSettings: _*)
             |  .configs(MultiJvm)
             |  .dependsOn(common % "test->test;compile->compile")
             |""".stripMargin
        }.mkString("", "\n", "\n")
    }
  }

  def genBuildFile(projectBaseFolder: File, exercises: Vector[String], multiJVM: Boolean = false): Unit = {
    val buildDef =
      s"""
         |lazy val base = (project in file("."))
         |  .aggregate(
         |    common,
         |${exercises.mkString("    ", ",\n    ", "")}
         | )
         |  .settings(scalaVersion in ThisBuild := Version.scalaVersion)
         |  .settings(CommonSettings.commonSettings: _*)
         |${if (multiJVM)
           s"""  .settings(SbtMultiJvm.multiJvmSettings: _*)
              |  .configs(MultiJvm)""".stripMargin else ""}
              |
              |lazy val common = project
              |  .settings(CommonSettings.commonSettings: _*)
              |
              |${exerciseProjects(exercises, multiJVM)}""".stripMargin

    dumpStringToFile(buildDef, new File(projectBaseFolder, "build.sbt") getPath)

  }

  def dumpStringToFile(string: String, filePath: String): Unit = {
    Files.write(Paths.get(filePath), string.getBytes(StandardCharsets.UTF_8))
  }

  def isExerciseFolder(folder: File): Boolean = {
    ExercisePathSpec.findFirstIn(folder.getPath).isDefined
  }

  def getExerciseNames(masterRepo: File): Vector[String] = {
    val exerciseFolders = sbtio.listFiles(masterRepo, FoldersOnly()).filter(isExerciseFolder)
    exerciseFolders.map(folder => folder.getName).toVector.sorted
  }

  def activateAllExercises(state: State): State = {
    state
  }
}
