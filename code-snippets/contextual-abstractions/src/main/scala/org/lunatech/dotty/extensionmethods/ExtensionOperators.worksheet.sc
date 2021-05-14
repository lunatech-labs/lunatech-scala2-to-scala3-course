// Metals Scala worksheet

extension (a: String) def < (b: String): Boolean = a.compareTo(b) < 0
  
extension (a: Int) def  +++: (b: List[Int]) = a::b

println("abc" < "pqr")
println(1 +++: List(2,3,4))
