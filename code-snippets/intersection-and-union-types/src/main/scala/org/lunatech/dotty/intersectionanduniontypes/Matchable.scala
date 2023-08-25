package org.lunatech.dotty.intersectionanduniontypes

object Matchable {
  val iar: IArray[Int] = IArray(1, 2)

  iar.mkString("IArray(", ",", ")")

  iar match
    case a: Array[Int] => a(0) = 7

  iar.mkString("IArray(", ",", ")")
}
