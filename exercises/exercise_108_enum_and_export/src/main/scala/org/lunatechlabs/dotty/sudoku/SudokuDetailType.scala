package org.lunatechlabs.dotty.sudoku

sealed trait SudokuDetailType
case class Row(id: Int) extends SudokuDetailType
case class Column(id: Int) extends SudokuDetailType
case class Block(id: Int) extends SudokuDetailType
