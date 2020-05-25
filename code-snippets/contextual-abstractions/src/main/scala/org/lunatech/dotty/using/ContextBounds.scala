package org.lunatech.dotty.using

trait Ord[T] {
  def compare(x: T, y: T): Int
  def (x: T) < (y: T) = compare(x, y) < 0
  def (x: T) > (y: T) = compare(x, y) > 0
}

given ordInt as Ord[Int] {
  def compare(x: Int, y: Int) = x - y
}

object ContextBounds {
  def max[T: Ord](x: T, y: T): T =
    if (x > y) x else y

  // The following will still be allowed in Scala 3.0, but disallowed in 3.1
  //@main def contextBoundsMain = println(max(1,3)(ordInt))

  // If you want to explicitly pass an instance of Ord[Int], do it as follows
  @main def contextBoundsMain = println(max(1,3)(using ordInt))
}
