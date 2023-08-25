package org.lunatech.dotty.exportclause

object A {
  object B {
    object C {
      val x: Int = 5
    }
  }
  export B.C.x
}

@main def test: Unit =
  import A._
  println(x)
