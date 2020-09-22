package org.lunatech.dotty.multiversalequality

import java.util.UUID

/**
 * In this example we imagine that we refactored 'Item' so that now we use
 * UUID instead of our custom-made 'Id' value class to identify items.
 * But in the refactor we forgot to change the Repository.findById.
 * This will type-check but we will not find the repository
 */
object Bad1 {
  final case class Id(value: Long) extends AnyVal
  final case class Item(id: UUID)

  class Repository(items: Seq[Item]) {
    def findById(id: Id): Option[Item] = {
      items.find(_.id == id)
    }
  }
}

/**
 * Same as above but now we import strictEquality in the scope. This will now
 * fail to compile because we don't have any Eql typeclass instances for
 * comparing 'Id' with UUID
 */
object Bad2 {
  import scala.language.strictEquality

  final case class Id(value: Long) extends AnyVal
  final case class Item(id: UUID)

  class Repository(items: Seq[Item]) {

    /** COMMENTED OUT. DOES NOT COMPILE.

    def findById(id: Id): Option[Item] = {
      items.find(_.id == id)
    }

    */
  }
}
