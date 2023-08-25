package org.lunatech.dotty.intersectionanduniontypes

enum Tools {
  case Hammer(size: Int)
  case Screwdriver(size: Int)
}
enum ToolSupplies {
  case Nail(size: Int)
  case Screw(size: Int)
}
def printIt(t: Tools | ToolSupplies): Unit = {
  t match
    case tool: Tools          => println(s"Got a tool: $tool")
    case supply: ToolSupplies => println(s"Got a supply: $supply")
}

@main def simpleUnionTypes: Unit = {
  import Tools._
  import ToolSupplies._

  printIt(Hammer(6))
  printIt(Nail(9))
}
