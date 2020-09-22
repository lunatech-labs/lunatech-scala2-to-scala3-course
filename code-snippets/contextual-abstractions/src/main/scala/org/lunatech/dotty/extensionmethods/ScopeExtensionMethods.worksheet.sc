trait IntOps:
  extension (i: Int) def isZero: Boolean = i == 0

  extension (i: Int) def safeMod(x: Int): Option[Int] =
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
  
  val d1 = 25.divide(3)
  val d2 = 25.divide(0)

SafeDiv.d1
SafeDiv.d2

given IntOps

20.safeMod(0)
20.safeMod(3)

extension [T](xs: List[List[T]])
  def flatten: List[T] = xs.foldLeft(Nil: List[T])(_ ++ _)

given [T: Ordering] as Ordering[List[T]]:
  override def compare(l1: List[T], l2: List[T]): Int = 1
  extension (xs: List[T])
    def < (ys: List[T]): Boolean = summon[Ordering[T]].lt(xs.head, ys.head)


// extension method available since it is in the implicit scope of List[List[Int]]
List(List(1, 2), List(3, 4)).flatten

// extension method available since it is in the given Ordering[List[T]],
// which is itself in the implicit scope of List[Int]
List(1, 2) < List(3)