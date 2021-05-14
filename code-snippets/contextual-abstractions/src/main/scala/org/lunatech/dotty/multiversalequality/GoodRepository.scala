package org.lunatech.dotty.multiversalequality

import java.util.UUID

object Good {
  import scala.language.strictEquality

  final case class Id(value: Long) extends AnyVal
  final case class Item(id: UUID)

  given CanEqual[UUID, UUID] = CanEqual.derived

  class Repository(items: Seq[Item]) {
    def findById(id: UUID): Option[Item] = {
      items.find(_.id == id)
    }
  }
}
