package org.lunatech.dotty.extensionmethods

extension [T](xs: List[T])
  def second =
    xs.tail.head

extension [T](xs: List[List[T]])
  def flattened =
    xs.foldLeft[List[T]](Nil)(_ ++ _)

extension [T: Numeric](x: T)
  def  + (y: T): T =
    summon[Numeric[T]].plus(x, y)

def [T: Numeric](x: T) - (y: T) = summon[Numeric[T]].plus(x, y)

case class Circle(x: Double, y: Double, radius: Double)
def (c: Circle).circumference: Double = c.radius * math.Pi * 2

@main def circles() =
  println(s"Circle: ${Circle(1.0, 2.0, 4).circumference}")