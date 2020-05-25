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
import sbt._
import sbt.complete.DefaultParsers._

import scala.Console
import scala.util.matching._

object Man {
  val manDetail: String = "Displays the README.md file. Use <noarg> for setup README.md or <e> for exercise README.md"

  lazy val optArg = OptSpace ~> StringBasic.?

  def man: Command = Command("man")(_ => optArg) { (state, arg) =>
    arg match {
      case Some(a) if a == "e" =>
        val base: File = Project.extract(state).get(sourceDirectory)
        val readmeFile: File = {
          val bPath = new File(base, "/test/resources/README.md")
          if (bPath.isFile) bPath else new File(base, "../README.md")
        }
        printOut(readmeFile)
        Console.print("\n")
        state
      case Some(a) =>
        Console.print("\n")
        Console.println(Console.RED + "[ERROR] " + Console.RESET + "invalid argument " + Console.RED + a + Console.RESET  + ". " + manDetail)
        Console.print("\n")
        state
      case None =>
        val base: File = Project.extract(state).get(baseDirectory)
        val readMeFile = new sbt.File(new sbt.File(Project.extract(state).structure.root), "README.md")
        printOut(readMeFile)
        Console.print("\n")
        state
    }
  }

  val bulletRx: Regex = """- """.r
  val boldRx: Regex = """(\*\*)(\w*)(\*\*)""".r
  val codeRx: Regex = """(`)([^`]+)(`)""".r
  val fenceStartRx: Regex = """^```(bash|scala)$""".r
  val fenceEndRx: Regex = """^```$""".r
  val numberRx: Regex = """^(\d{1,3})(\. )""".r
  val urlRx: Regex = """(\()(htt[a-zA-Z0-9\-\.\/:]*)(\))""".r
  val ConBlue = Console.BLUE
  val ConGreen = Console.GREEN
  val ConMagenta = Console.MAGENTA
  val ConRed = Console.RED
  val ConReset = Console.RESET
  val ConYellow = Console.YELLOW

  def printOut(path: File) {
    var inCodeFence = false
    IO.readLines(path) foreach {
      case ln if !inCodeFence && ln.length > 0 && ln(0).equals('#') =>
        Console.println(ConRed + ln + ConReset)
      case ln if !inCodeFence && ln.matches(".*" + bulletRx.toString() + ".*") =>
        val lne = bulletRx replaceAllIn (ln, ConRed + bulletRx.toString() + ConReset)
        Console.println(rxFormat(rxFormat(rxFormat(lne, codeRx, ConGreen), boldRx, ConYellow), urlRx, ConMagenta,
          keepWrapper = true))
      case ln if !inCodeFence && ln.matches(numberRx.toString() + ".*") =>
        val lne = numberRx replaceAllIn (ln, _ match { case numberRx(n, s) => f"$ConRed$n$s$ConReset" })
        Console.println(rxFormat(rxFormat(lne, codeRx, ConGreen), boldRx, ConYellow))
      case ln if ln.matches(fenceStartRx.toString()) =>
        inCodeFence = true
        Console.print(ConGreen)
      case ln if ln.matches(fenceEndRx.toString()) =>
        inCodeFence = false
        Console.print(ConReset)
      case ln =>
        Console.println(rxFormat(rxFormat(rxFormat(ln, codeRx, ConGreen), boldRx, ConYellow), urlRx, ConMagenta,
          keepWrapper = true))
    }
  }

  def rxFormat(ln: String, rx: Regex, startColor: String, keepWrapper: Boolean = false): String = ln match {
    case `ln` if ln.matches(".*" + rx.toString + ".*") =>
      val lne = rx replaceAllIn (ln, _ match {
        case rx(start, in, stop) =>
          if (keepWrapper)
            f"$start$startColor$in$ConReset$stop"
          else
            f"$startColor$in$ConReset"
      })
      lne
    case _ =>
      ln
  }

}

