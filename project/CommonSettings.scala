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

//import com.lightbend.cinnamon.sbt.Cinnamon
//import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys
import sbt.Keys._
import sbt._
import sbtstudent.AdditionalSettings
import com.typesafe.sbt.packager.archetypes.{JavaAppPackaging, JavaServerAppPackaging}
import com.typesafe.sbt.packager.docker.DockerChmodType.UserGroupWriteExecute
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.{dockerAdditionalPermissions, dockerBaseImage, dockerChmodType, dockerCommands, dockerEnvVars, dockerExposedPorts, dockerRepository}
import com.typesafe.sbt.packager.docker.{Cmd, DockerChmodType, DockerPlugin}
import com.typesafe.sbt.packager.universal.UniversalPlugin, UniversalPlugin.autoImport._

object CommonSettings {
  lazy val commonSettings = Seq(
    organization := "com.lightbend.training",
    version := "1.3.0",
    scalaVersion := Version.scalaVer,
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
    publishArtifact in packageSrc := false,
    publishArtifact in packageDoc := false,
    libraryDependencies ++= Dependencies.dependencies,
//    credentials += Credentials(Path.userHome / ".lightbend" / "commercial.credentials"),
//    resolvers += "com-mvn" at "https://repo.lightbend.com/commercial-releases/",
//    resolvers += Resolver.url("com-ivy", url("https://repo.lightbend.com/commercial-releases/"))(Resolver.ivyStylePatterns)
  ) ++
    AdditionalSettings.initialCmdsConsole ++
    AdditionalSettings.initialCmdsTestConsole ++
    AdditionalSettings.cmdAliases

  lazy val configure: Project => Project = (proj: Project) => {
    proj
    //.enablePlugins(Cinnamon)
    .settings(CommonSettings.commonSettings: _*)
//    .settings(
//      libraryDependencies += Cinnamon.library.cinnamonPrometheus,
//      libraryDependencies += Cinnamon.library.cinnamonPrometheusHttpServer,
//      libraryDependencies += Cinnamon.library.cinnamonAkkaHttp,
//      libraryDependencies += Cinnamon.library.cinnamonOpenTracingZipkin,
//      libraryDependencies += Cinnamon.library.cinnamonCHMetricsElasticsearchReporter
//    )
      .enablePlugins(DockerPlugin, JavaAppPackaging)
      .settings(
        mappings in Universal ++=
          Seq(
            file("nodeFiles/librpi_ws281x.so") -> "lib/librpi_ws281x.so",
            file("sudokus/001.sudoku") -> "sudokus/001.sudoku"
          ),
        javaOptions in Universal ++=
          Seq(
            "-Djava.library.path=lib",
            "-Dcluster-node-configuration.cluster-id=cluster-0",
            "-Dcluster-status-indicator.led-strip-type=ten-led-non-reversed-order"
          ),
        //dockerBaseImage := "arm32v7/openjdk",
        dockerBaseImage := "arm32v7/adoptopenjdk",
        dockerCommands ++= Seq( Cmd("USER", "root"),
          Cmd("RUN", "mkdir -p","/dev/mem")  ),
        dockerChmodType := UserGroupWriteExecute,
        dockerRepository := Some("docker-registry-default.gsa2.lightbend.com/lightbend"),
        dockerExposedPorts := Seq(8080, 8558, 2550, 9001),
        dockerAdditionalPermissions ++= Seq((DockerChmodType.UserGroupPlusExecute, "/tmp"))
      )
  }
}
