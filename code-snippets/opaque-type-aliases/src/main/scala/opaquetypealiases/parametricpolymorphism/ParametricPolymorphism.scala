package opaquetypelaliases.parametricpolymorphism

case class Kilometres(value: Double) extends AnyVal
case class Miles(value: Double) extends AnyVal

class Rocket(distanceTravelled: Kilometres):
  def advance(distanceToAdvance: Kilometres): Rocket = new Rocket(
    Kilometres(distanceTravelled.value + distanceToAdvance.value))

type Conversion[A] = A => Kilometres
class Booster():
  def advanceRocket[A: Conversion](rocket: Rocket, distanceToAdvance: A): Rocket = {
    val distanceInKm = summon[Conversion[A]](distanceToAdvance)
    rocket.advance(distanceInKm)
  }

val rocket1 = new Rocket(Kilometres(0))
val rocket2 = new Rocket(Kilometres(0))
val booster = new Booster()

given Conversion[Kilometres] = identity
given Conversion[Miles] = miles => Kilometres(miles.value * 1.6)

val r1 = booster.advanceRocket(rocket1, Kilometres(100)) // Allocation of Kilometres object
val r2 = booster.advanceRocket(rocket2, Miles(200))
