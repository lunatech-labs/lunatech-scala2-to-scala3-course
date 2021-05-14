// Metals Scala worksheet

import scala.annotation.targetName

extension [T](xs: List[T])
  def second =
    xs.tail.head

extension [T](xs: List[List[T]])
  def flattened =
    xs.foldLeft[List[T]](Nil)(_ ++ _)

extension [T: Numeric](x: T)
  def +++ (y: T): T =
    summon[Numeric[T]].plus(x, y)
  def --- (y: T): T = 
    summon[Numeric[T]].minus(x, y)

1 --- 2
2 +++ 1

case class Circle(x: Double, y: Double, radius: Double)

extension (c: Circle) 
  def circumference: Double = c.radius * math.Pi * 2

Circle(1.0, 2.0, 4).circumference