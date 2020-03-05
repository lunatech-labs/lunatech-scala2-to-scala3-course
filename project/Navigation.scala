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

import sbt._
import scala.Console

object Navigation {

  val setupNavAttrs: (State) => State = (state: State) => state

  val loadBookmark: (State) => State = (state: State) => {
    // loadBookmark doesn't really load a bookmark for a master repo.
    // It just selects the first exercise (project) from the repo
    val refs =
    Project.extract(state)
      .structure
      .allProjectRefs
      .toList
      .map(r => r.project)
      // By convention, a project exercise has a 3-digit number in it enclosed in underscores
      .filter(_.matches(""".*_\d{3}_.*"""))
      .sorted
    if (refs.nonEmpty)
      Command.process(s"project ${refs.head}", state)
    else {
      // No project was found adhering to the naming convention
      println(s"\n${Console.RED}No projects found!${Console.RESET}\n")
      state
    }
  }
}
