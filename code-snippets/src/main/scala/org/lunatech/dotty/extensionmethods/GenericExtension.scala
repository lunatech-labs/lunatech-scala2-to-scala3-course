package org.lunatech.dotty.extensionmethods

def [T](xs: List[T]) second =
  xs.tail.head

def [T](xs: List[List[T]]) flattened =
  xs.foldLeft[List[T]](Nil)(_ ++ _)

def [T: Numeric](x: T) + (y: T): T =
  summon[Numeric[T]].plus(x, y)


