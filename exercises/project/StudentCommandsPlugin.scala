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

import sbt.Keys._
import sbt.{Def, _}

import scala.Console

object StudentCommandsPlugin extends AutoPlugin {

  override val requires = sbt.plugins.JvmPlugin
  override val trigger: PluginTrigger = allRequirements
  object autoImport {
  }
  override lazy val globalSettings =
    Seq(
      commands in Global ++=
        Seq(
          Man.man, MPSelection.activateAllExercises, MPSelection.setActiveExerciseNr
        ),
      onLoad in Global := {
        val state = (onLoad in Global).value
        Navigation.loadBookmark compose(Navigation.setupNavAttrs compose state)
      }
    )

  def extractCurrentExerciseDesc(state: State): String = {
    val currentExercise =  Project.extract(state).currentProject.id

    currentExercise
      .replaceFirst("""^.*_\d{3}_""", "")
      .replaceAll("_", " ")
  }

  def extractProjectName(state: State): String = {
    IO.readLines(new sbt.File(new sbt.File(Project.extract(state).structure.root), ".courseName")).head
  }

  def renderCMTPrompt(state: State) = {
    val exercise = Console.GREEN + extractCurrentExerciseDesc(state) + Console.RESET
    val manRmnd = Console.GREEN + "man [e]" + Console.RESET
    val prjNbrNme = extractProjectName(state)
    s"$manRmnd > $prjNbrNme > $exercise > "
  }

  override def projectSettings: Seq[Def.Setting[State => String]] =
    Seq(
      shellPrompt := { state => renderCMTPrompt(state)}
    )
}
