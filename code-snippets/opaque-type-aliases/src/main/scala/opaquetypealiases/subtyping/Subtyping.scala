package opaquetypelaliases.subtyping

sealed trait Distance extends Any
case class Kilometres(value: Double) extends AnyVal with Distance
case class Miles(value: Double) extends AnyVal with Distance

class Rocket(distanceTravelled: Kilometres):
  def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
    Kilometres(distanceTravelled.value + distanceToAdvance.value))

class Booster():
  def advanceRocket(rocket: Rocket, distanceToAdvance: Distance): Rocket = {
    val distanceInKm = distanceToAdvance match {
      case miles: Miles   => Kilometres(miles.value * 1.6)
      case km: Kilometres => km
    }
    rocket.advance(distanceInKm)
  }

val rocket1 = new Rocket(Kilometres(0))
val rocket2 = new Rocket(Kilometres(0))
val booster = new Booster()

val r1 = booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
val r2 = booster.advanceRocket(rocket2, Miles(200)) // Allocation of Miles object
