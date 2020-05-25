package org.lunatech.dotty.exportclause

object A {
  object B {
    object C {
      val x: Int = 5
    }
  }
  export B.C.x
}
