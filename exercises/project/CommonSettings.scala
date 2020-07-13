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
import sbt._
import sbtstudent.AdditionalSettings
import dotty.tools.sbtplugin.DottyPlugin.autoImport.DottyCompatModuleID
import sbtstudent.StudentCommandsPlugin._


object CommonSettings {
  lazy val commonSettings = Seq(
    scalacOptions in Compile ++= CompileOptions.compileOptions,
    javacOptions in Compile ++= Seq("--release", "8"),
    unmanagedSourceDirectories in Compile := List((scalaSource in Compile).value, (javaSource in Compile).value),
    unmanagedSourceDirectories in Test := List((scalaSource in Test).value, (javaSource in Test).value),
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
    logBuffered in Test := false,
    parallelExecution in Test := false,
    parallelExecution in GlobalScope := false,
    parallelExecution in ThisBuild := false,
    fork in Test := false,
    run / connectInput := true,
    publishArtifact in packageSrc := false,
    publishArtifact in packageDoc := false,
    libraryDependencies ++= Dependencies.dependencies,
    libraryDependencies ++= Dependencies.crossDependencies.map(_.withDottyCompat(scalaVersion.value)),
    testFrameworks += new TestFramework("munit.Framework"),
    shellPrompt := (state => renderCMTPrompt(state))
  ) ++
    AdditionalSettings.initialCmdsConsole ++
    AdditionalSettings.initialCmdsTestConsole ++
    AdditionalSettings.cmdAliases

  lazy val configure: Project => Project = (project: Project) => {
    project.settings(CommonSettings.commonSettings: _*)
  }
}
