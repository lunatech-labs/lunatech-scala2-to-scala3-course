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
import sbtstudent.StudentCommandsPlugin._


object CommonSettings {
  lazy val commonSettings = Seq(
    Compile / scalacOptions ++= CompileOptions.compileOptions,
    Compile / javacOptions ++= Seq("--release", "8"),
    Compile / unmanagedSourceDirectories := List((Compile / scalaSource).value, (Compile / javaSource).value),
    Test / unmanagedSourceDirectories := List((Test / scalaSource).value, (Test / javaSource).value),
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
    Test / logBuffered := false,
    Test / parallelExecution := false,
    GlobalScope / parallelExecution := false,
    Test / fork := false,
    run / connectInput := true,
    packageSrc / publishArtifact := false,
    packageDoc / publishArtifact := false,
    libraryDependencies ++= Dependencies.dependencies,
    libraryDependencies ++= Dependencies.crossDependencies.map(_.cross(CrossVersion.for3Use2_13)),
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
