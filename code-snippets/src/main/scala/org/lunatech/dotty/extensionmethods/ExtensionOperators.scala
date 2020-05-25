package org.lunatech.dotty.extensionmethods

object ExtensionOperators extends App {
  def (a: String) < (b: String): Boolean = a.compareTo(b) < 0
  
  def (a: Int) +: (b: List[Int]) = a::b

  println("abc" < "pqr")  // true

  val lst = 1 +: List(2,3,4)  // val lst: List[Int] = List(1, 2, 3, 4)
}
