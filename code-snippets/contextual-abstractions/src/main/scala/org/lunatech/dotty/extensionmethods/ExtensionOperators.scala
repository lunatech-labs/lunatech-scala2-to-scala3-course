package org.lunatech.dotty.extensionmethods

extension (a: String) def < (b: String): Boolean = a.compareTo(b) < 0
  
extension (a: Int) def  +++: (b: List[Int]) = a::b

@main def extensionOperators() =
  println("abc" < "pqr")  // true
  println(1 +++: List(2,3,4))  // val lst: List[Int] = List(1, 2, 3, 4)
