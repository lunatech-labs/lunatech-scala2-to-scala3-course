package org.lunatech.dotty.enumeration

// A simple enumeration of values
object Enum {
  enum Color {
    case Red, Green, Blue
  }

  @main def printInfo(): Unit = {
    val red = Color.Red
    println(s"""Ordinal of Red = ${red.ordinal}""")

    // Get all values in enumeration
    val enums: Array[Color] = Color.values
    println(s"Values in enumeration Color: ${enums.mkString("Array(", ", ", ")")}")

    // Access an value by its name
    val blue = Color.valueOf("Blue")
    println(s"""Ordinal of "Blue" = ${blue.ordinal}""")
  }
}

// A parametrized version of the same enumeration
object ParametrizedEnum {

  enum Color(val rgb: Int) {
    case Red extends Color(0xff0000)
    case Green extends Color(0x00ff00)
    case Blue extends Color(0x0000ff)
  }
}

enum Planet(mass: Double, radius: Double) {
  private final val G = 6.67300e-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) = otherMass * surfaceGravity

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Venus extends Planet(4.869e+24, 6.0518e6)
  case Earth extends Planet(5.976e+24, 6.37814e6)
  case Mars extends Planet(6.421e+23, 3.3972e6)
  case Jupiter extends Planet(1.9e+27, 7.1492e7)
  case Saturn extends Planet(5.688e+26, 6.0268e7)
  case Uranus extends Planet(8.686e+25, 2.5559e7)
  case Neptune extends Planet(1.024e+26, 2.4746e7)
}

object Planet {
  def main(args: Array[String]) = {
    val earthWeight = args(0).toDouble
    val mass = earthWeight / Earth.surfaceGravity
    for (p <- values)
      println(s"Your weight on $p is ${p.surfaceWeight(mass)}")
  }
}

object JavaEnumsAsScalaEnums {
  enum Color extends java.lang.Enum[Color] {
    case Red, Green, Blue
  }

  Color.Green.compareTo(Color.Green)

  Color.Green.compareTo(Color.Blue)
}
