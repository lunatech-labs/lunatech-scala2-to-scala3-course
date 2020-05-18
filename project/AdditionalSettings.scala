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

package sbtstudent

import sbt.Keys._
import sbt._

object AdditionalSettings {

  // Change 'loadInitialCmds' to true when requested in exercise instructions
  val loadInitialCmds = false

  val initialCmdsConsole: Seq[Def.Setting[String]] =
    if (loadInitialCmds) {
      Seq(initialCommands in console := "import ._")
    } else {
      Seq()
    }

  val initialCmdsTestConsole: Seq[Def.Setting[String]]  =
    if (loadInitialCmds) {
      Seq(initialCommands in(Test, console) := (initialCommands in console).value + ", TestData._")
    } else {
      Seq()
    }

  // Note that if no command aliases need to be added, assign an empty Seq to cmdAliasesIn
  val cmdAliasesIn: Seq[Def.Setting[(State) => State]] = Seq(
    addCommandAlias("runSolver", "runMain org.lunatechlabs.dotty.SudokuSolverMain"),
  ).flatten

  val cmdAliases: Seq[Def.Setting[(State) => State]] =
    cmdAliasesIn
}
