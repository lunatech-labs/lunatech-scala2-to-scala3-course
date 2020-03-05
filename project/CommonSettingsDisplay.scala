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

object CommonSettingsDisplay {

   lazy val commonSettings = Seq (
      libraryDependencies ++= Seq(
         "com.typesafe.akka" %% "akka-actor" % Version.akkaVer,

         "com.pi4j" % "pi4j-core" % "1.2",
         "com.pi4j" % "pi4j-device" % "1.2",
         "com.pi4j" % "pi4j-gpio-extension" % "1.2",

         "ch.qos.logback" % "logback-classic" % "1.2.3",
         "org.apache.commons" % "commons-lang3" % "3.1",
         "commons-io" % "commons-io" % "2.5",
         "org.scalatest" %% "scalatest" % "3.0.8" % Test,
         "org.scalamock" %% "scalamock" % "4.4.0" % Test,
         "org.powermock" % "powermock-api-mockito2" % "2.0.2" % Test,

         "com.typesafe.akka" %% "akka-cluster" % Version.akkaVer,
         "com.lightbend.akka.management" %% "akka-management" % "1.0.3",
         "com.typesafe.akka" %% "akka-cluster-sharding" % Version.akkaVer,
         "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.5.2",
         "mysql" % "mysql-connector-java" % "8.0.18",
         "com.lightbend.akka.management" %% "akka-management-cluster-http" % "1.0.3",
         "com.typesafe.akka" %% "akka-distributed-data" % "2.5.26"
      ))
}

