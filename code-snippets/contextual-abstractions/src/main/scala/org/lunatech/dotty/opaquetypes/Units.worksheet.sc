// Metals Scala worksheet

object Scala2TypeAliases:
  type Kilometres = Double
  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  type Miles = Double
  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      // Kilometres and Miles are transparent. They are both Double so this bug is allowed
      rocket.advance(distanceToAdvance)
    }

object Scala2ClassWrappers:
  case class Kilometres(value: Double)
  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      Kilometres(distanceTravelled.value + distanceToAdvance.value)
    )

    case class Miles(value: Double)
    class Booster() {
      /** COMMENTED OUT. DOES NOT COMPILE. */

      // def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
      //   // Kilometres and Miles are different types. So compiler prevents this bug
      //   rocket.advance(distanceToAdvance)
      // }


    }
end Scala2ClassWrappers

// object Scala2ValueClassWrappers:
//   case class Kilometres(value: Double) extends AnyVal
//   class Rocket(distanceTravelled: Kilometres) {
//     def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
//       Kilometres(distanceTravelled.value + distanceToAdvance.value)
//     )
//   }

//   case class Miles(value: Double) extends AnyVal
//   class Booster() {
//     /** COMMENTED OUT. DOES NOT COMPILE. */

//     // def advanceRocket(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
//     //   // Kilometres and Miles are different types. So compiler prevents this bug
//     //   rocket.advance(distanceToAdvance)
//     // }
//   }
// end Scala2ValueClassWrappers

// object Scala2ValueClassWrappersAllocationCases1 {
//   sealed trait Distance extends Any
//   case class Kilometres(value: Double) extends AnyVal with Distance
//   case class Miles(value: Double) extends AnyVal with Distance

//   class Rocket(distanceTravelled: Kilometres) {
//     def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
//       Kilometres(distanceTravelled.value + distanceToAdvance.value)
//     )
//   }

//   class Booster() {
//     def advanceRocket(rocket: Rocket, distanceToAdvance: Distance): Rocket = {
//       val distanceInKm = distanceToAdvance match {
//         case miles: Miles => Kilometres(miles.value * 1.6)
//         case km: Kilometres => km
//       }
//       rocket.advance(distanceInKm)
//     }
//   }

//   val rocket1 = new Rocket(Kilometres(0))
//   val rocket2 = new Rocket(Kilometres(0))
//   val booster = new Booster()

//   booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
//   booster.advanceRocket(rocket2, Miles(200)) // Allocation of Miles object
// }

// object Scala2ValueClassWrappersAllocationCases2 {
//   case class Kilometres(value: Double) extends AnyVal
//   case class Miles(value: Double) extends AnyVal

//   class Rocket(distanceTravelled: Kilometres) {
//     def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
//       Kilometres(distanceTravelled.value + distanceToAdvance.value)
//     )
//   }

//   type Conversion[A] = A => Kilometres
//   class Booster() {
//     def advanceRocket[A: Conversion](rocket: Rocket, distanceToAdvance: A): Rocket = {
//       val distanceInKm = summon[Conversion[A]](distanceToAdvance)
//       rocket.advance(distanceInKm)
//     }
//   }

//   val rocket1 = new Rocket(Kilometres(0))
//   val rocket2 = new Rocket(Kilometres(0))
//   val booster = new Booster()

//   given Conversion[Kilometres] = identity
//   given Conversion[Miles] = miles => Kilometres(miles.value * 1.6)

//   booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
//   booster.advanceRocket(rocket2, Miles(200)) // Allocation of Miles object

// }

// object Scala2ValueClassWrappersAllocationCases3 {
//   case class Kilometres(value: Double) extends AnyVal
//   case class Miles(value: Double) extends AnyVal

//   val distances: Array[Kilometres] = Array(Kilometres(10)) // Allocation of Kilometres object
// }

import scala.annotation.targetName

object Scala3OpaqueTypeAliasesDefinitions:
  opaque type Kilometres = Double
  object Kilometres {
    def apply(d: Double): Kilometres = d
  }

  opaque type Miles = Double
  object Miles {
    def apply(d: Double): Miles = d
  }

  extension (a: Kilometres)
    @targetName("plusKm")
    def +(b: Kilometres): Kilometres = a + b

    def toMiles: Miles = a / 1.6

  extension (a: Miles)
    @targetName("plusMiles")  
    def +(b: Miles): Miles = a + b

    def toKm: Kilometres = a * 1.6

end Scala3OpaqueTypeAliasesDefinitions

object Scala3OpaqueTypeAliasesTypeSafety:
  import Scala3OpaqueTypeAliasesDefinitions._

  class Rocket(distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  class Booster():
    def advanceRocket(rocket: Rocket, distanceToAdvance: Kilometres): Rocket = {
      // Kilometres and Miles are different types. So compiler prevents this bug
      rocket.advance(distanceToAdvance)
    }
    // The following doesn't compile: it actually catches a bug
    // def advanceRocket_bis(rocket: Rocket, distanceToAdvance: Miles): Rocket = {
    //   // Kilometres and Miles are different types. So compiler prevents this bug
    //   rocket.advance(distanceToAdvance)
    // }

end Scala3OpaqueTypeAliasesTypeSafety

object Scala3OpaqueTypeAliasesNoAllocations1:
  export Scala3OpaqueTypeAliasesDefinitions._

  class Rocket(val distanceTravelled: Kilometres):
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )

  type Distance = Kilometres | Miles
  class Booster():
    // THIS GIVES A WARNING. THE 'Kilometres' CASE IS UNREACHABLE due to erasure.
    // SO WE HAVE A BUG. Any 'Kilometres' passed to this method will be multiplied by 1.6
    def advanceRocket(rocket: Rocket, distanceToAdvance: Distance): Rocket = {
      val distanceInKm = distanceToAdvance match {
        case miles: Miles => miles.toKm
        case km: Kilometres => km
      }
      rocket.advance(distanceInKm)
    }

  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  val result1 = booster.advanceRocket(rocket1, Kilometres(100)) // No allocation of Kilometres object, but WRONG RESULT
  val result2 = booster.advanceRocket(rocket2, Miles(200)) // No allocation of Miles object

end Scala3OpaqueTypeAliasesNoAllocations1

Scala3OpaqueTypeAliasesNoAllocations1.result1.distanceTravelled
Scala3OpaqueTypeAliasesNoAllocations1.result2.distanceTravelled

object Scala3OpaqueTypeAliasesNoAllocations2 {
  import Scala3OpaqueTypeAliasesDefinitions._

  class Rocket(val distanceTravelled: Kilometres) {
    def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
      distanceTravelled + distanceToAdvance
    )
  }

  type Conversion[A] = A => Kilometres
  class Booster() {
    def advanceRocket[A: Conversion](rocket: Rocket, distanceToAdvance: A): Rocket = {
      val distanceInKm = summon[Conversion[A]](distanceToAdvance)
      rocket.advance(distanceInKm)
    }
  }

  val rocket1 = new Rocket(Kilometres(0))
  val rocket2 = new Rocket(Kilometres(0))
  val booster = new Booster()

  given Conversion[Kilometres] = identity
  given Conversion[Miles] = _.toKm

  val result1 = booster.advanceRocket[Kilometres](rocket1, Kilometres(100)) // No allocation of Kilometres object
  val result2 = booster.advanceRocket(rocket2, Miles(200)) // No allocation of Miles object

}

Scala3OpaqueTypeAliasesNoAllocations2.result1.distanceTravelled
Scala3OpaqueTypeAliasesNoAllocations2.result2.distanceTravelled

object Scala3OpaqueTypeAliasesNoAllocations3 {
  export Scala3OpaqueTypeAliasesDefinitions._

  val distances: Array[Kilometres] = Array(Kilometres(10)) // No allocation of Kilometres object

}
