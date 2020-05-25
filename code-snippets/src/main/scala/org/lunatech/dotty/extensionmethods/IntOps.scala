package org.lunatech.dotty.extensionmethods

trait IntOps {
  def (i: Int).isEven: Boolean = i % 2 == 0

  def (i: Int).unary_! : Int = -i
}

object IntOperations {

    // 1. An extension method is applicable if it is visible under a simple name, 
    //    by being defined or inherited or imported in a scope enclosing the application

    // 2. An extension method is applicable if it is a member of some given instance
    //    at the point of the application.
    
    // we could define this object as `IntOperations extends IntOps`.
    // A more elegant way to bring `IntOps` to the context is by using a `given`

    given IntOps

    @main def show(): Unit = {
        println(2.isEven)  // true
        println(!5)        // -5
    }
}
