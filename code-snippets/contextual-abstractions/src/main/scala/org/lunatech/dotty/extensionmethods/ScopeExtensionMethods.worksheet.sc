trait IntOps:
  extension (i: Int) 
    def isZero: Boolean = i == 0

    def safeMod(x: Int): Option[Int] =
      // extension method defined in same scope IntOps
      if x.isZero then None
      else Some(i % x)

object IntOpsEx extends IntOps:
  extension (i: Int) def safeDiv(x: Int): Option[Int] =
    // extension method brought into scope via inheritance from IntOps
    if x.isZero then None
    else Some(i / x)

object SafeDiv:
  import IntOpsEx._ // brings safeDiv and safeMod into scope

  extension (i: Int) def divide(d: Int) : Option[(Int, Int)] =
    // extension methods imported and thus in scope
    (i.safeDiv(d), i.safeMod(d)) match
      case (Some(d), Some(r)) => Some((d, r))
      case _ => None

import SafeDiv._
val n = 9
val d1 = 25.divide(3)
val d2 = 25.divide(0)

given IntOps = new IntOps{}

20.safeMod(0)
20.safeMod(3)

extension [T](xs: List[List[T]])
  def flatten: List[T] = xs.foldLeft(Nil: List[T])(_ ++ _)

// extension method available since it is in the implicit scope of List[List[Int]]
List(List(1, 2), List(3, 4)).flatten
