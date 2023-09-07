package org.lunatechlabs.dotty.sudoku

import java.io.{BufferedReader, File, FileReader}
import java.util.NoSuchElementException

object SudokuIO:

  def sudokuPrinter(result: SudokuSolver.SudokuSolution): String =
    result.sudoku.sliding(3, 3).map(_.printSudokuRow).mkString("\n+---+---+---+\n", "+---+---+---+\n", "+---+---+---+")

  /*
   * FileLineTraversable code taken from "Scala in Depth" by Joshua Suereth
   */

  class FileLineTraversable(file: File) extends Iterable[String]:
    val fr = new FileReader(file)
    val input = new BufferedReader(fr)
    var cachedLine: Option[String] = None
    var finished: Boolean = false

    override def iterator: Iterator[String] = new Iterator[String]:

      override def hasNext: Boolean = (cachedLine, finished) match
        case (Some(_), _) => true

        case (None, true) => false

        case (None, false) =>
          try
            val line = input.readLine()
            if line == null then
              finished = true
              input.close()
              fr.close()
              false
            else
              cachedLine = Some(line)
              true
          catch
            case e: java.io.IOError =>
              throw new IllegalStateException(e.toString)

      override def next(): String =
        if !hasNext then throw new NoSuchElementException("No more lines in file")
        val currentLine = cachedLine.get
        cachedLine = None
        currentLine
    override def toString: String =
      "{Lines of " + file.getAbsolutePath + "}"

  def convertFromCellsToComplete(cellsIn: Vector[(String, Int)]): Vector[(Int, CellUpdates)] =
    for
      (rowCells, row) <- cellsIn
      updates = rowCells.zipWithIndex.foldLeft(cellUpdatesEmpty):
        case (cellUpdates, (c, index)) if c != ' ' =>
          (index, Set(c.toString.toInt)) +: cellUpdates
        case (cellUpdates, _) => cellUpdates
    yield (row, updates)

  def readSudokuFromFile(sudokuInputFile: java.io.File): Vector[(Int, CellUpdates)] =
    val dataLines = new FileLineTraversable(sudokuInputFile).toVector
    val cellsIn =
      dataLines
        .map { inputLine => """\|""".r.replaceAllIn(inputLine, "") } // Remove 3x3 separator character
        .filter(_ != "---+---+---") // Remove 3x3 line separator
        .map("""^[1-9 ]{9}$""".r.findFirstIn(_)) // Input data should only contain values 1-9 or ' '
        .collect { case Some(x) => x }
        .zipWithIndex

    convertFromCellsToComplete(cellsIn)
