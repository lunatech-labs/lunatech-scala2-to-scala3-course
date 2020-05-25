package org.lunatech.dotty.enumeration

enum Option[+T] {
  case Some(x: T)
  case None
}
