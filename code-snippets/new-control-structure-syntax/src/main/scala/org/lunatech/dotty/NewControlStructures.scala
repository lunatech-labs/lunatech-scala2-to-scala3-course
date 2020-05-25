package org.lunatech.dotty

object NewControlStructures {
  var x = 5
  def f(x: Int) = x * x + 5
  val xs = List.range(1,4)
  val ys = Vector.range(3,7)

  if (x < 0) "negative"
  else if (x == 0)
    "zero"
  else
    "positive"

  if (x < 0) -x else x

  while (x >= 0) x = f(x)

  for (x <- xs if x > 0)
    yield x * x

  for {
    x <- xs
    y <- ys
  } println(x + y)
  
}
