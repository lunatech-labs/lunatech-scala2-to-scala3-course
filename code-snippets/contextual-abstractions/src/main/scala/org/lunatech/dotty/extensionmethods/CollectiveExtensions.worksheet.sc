// Metals Scala worksheet

extension (i: Int)
  def isEven: Boolean = i % 2 == 0

  def unary_! : Int = -i

  def square : Int = i * i

extension [T](xs: List[T])
  def second: T = xs.tail.head
  def third: T = xs.tail.second

println(List(1,2,3).second)
println(! 5)
println(5.isEven)
println(5.square)