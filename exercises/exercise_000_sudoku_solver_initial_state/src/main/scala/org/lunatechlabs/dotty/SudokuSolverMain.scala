/** Copyright © 2020-2023 Lunatech Labs.
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  * the License. You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *
  * NO COMMERCIAL SUPPORT OR ANY OTHER FORM OF SUPPORT IS OFFERED ON THIS SOFTWARE BY Lunatech Labs.
  *
  * See the License for the specific language governing permissions and limitations under the License.
  */

package org.lunatechlabs.dotty

import akka.NotUsed
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import org.lunatechlabs.dotty.sudoku.{SudokuProblemSender, SudokuSolver, SudokuSolverSettings}
import kamon.Kamon

import scala.Console.{GREEN, RESET}
import scala.io.StdIn

object Main {
  def apply(): Behavior[NotUsed] =
    Behaviors.setup { context =>
      val sudokuSolverSettings = SudokuSolverSettings("sudokusolver.conf")
      // Start a SudokuSolver
      val sudokuSolver = context.spawn(SudokuSolver(sudokuSolverSettings), s"sudoku-solver")
      // Start a Sudoku problem sender
      context.spawn(SudokuProblemSender(sudokuSolver, sudokuSolverSettings), "sudoku-problem-sender")

      Behaviors.receiveSignal { case (_, Terminated(_)) =>
        Behaviors.stopped
      }
    }
}

object SudokuSolverMain {

  def main(args: Array[String]): Unit = {
    Kamon.init()

    val system = ActorSystem[NotUsed](Main(), "sudoku-solver-system")

    println(s"${GREEN}Hit RETURN to stop solver${RESET}")
    StdIn.readLine()
    system.terminate()
  }
}
