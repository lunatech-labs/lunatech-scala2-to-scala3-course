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

import sbt._

object Version {
  val akkaVer           = "2.6.4"
  val akkaHttpVer       = "10.1.11"
  val logbackVer        = "1.2.3"
  val akkaManagementVer = "1.0.6"
  val scalaTestVer      = "3.1.1"
  val scalaVersion = "0.24.0-RC1"
}

object Dependencies {

  private val akkaDeps = Seq(
    "com.typesafe.akka"             %% "akka-actor-typed",
    "com.typesafe.akka"             %% "akka-slf4j",
    "com.typesafe.akka"             %% "akka-stream",
  ).map (_ % Version.akkaVer)

  private val scalaTestDeps = Seq(
    "org.scalatest"                 %% "scalatest" % Version.scalaTestVer % Test
  )

  private val akkaTestkitDeps = Seq(
    "com.typesafe.akka"             %% "akka-actor-testkit-typed" % Version.akkaVer % Test
  )

  private val logbackDeps = Seq (
    "ch.qos.logback"                 %  "logback-classic",
  ).map (_ % Version.logbackVer)

  val crossDependencies: Seq[ModuleID] =
    akkaDeps ++
    scalaTestDeps ++
    akkaTestkitDeps

  val dependencies: Seq[ModuleID] =
    logbackDeps
}
