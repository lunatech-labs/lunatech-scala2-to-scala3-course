
import scala.language.implicitConversions

// Dotty given, using and summon which replaces Scala 2 `implicit` and 'implicitly'

sealed trait TemperatureScale
case class Celcius(value: Double) extends TemperatureScale
case class Farenheit(value: Double) extends TemperatureScale

// implicit conversions
// see: https://dotty.epfl.ch/docs/reference/contextual/conversions.html

given Conversion[Celcius, Farenheit] with {
  def apply(temperature: Celcius): Farenheit = Farenheit((temperature.value * 9/5) + 32)
}

given Conversion[Farenheit, Celcius] with {
  def apply(temperature: Farenheit): Celcius = Celcius((temperature.value - 32) * 5/9)
}

trait TemperatureConverter[T, U]:
  def convert[U](value: T)(using conversion: Conversion[T, U]): U

given TemperatureConverter[Farenheit, Celcius] with {
  override def convert[Celcius](temperature: Farenheit)(using conversion: Conversion[Farenheit, Celcius]): Celcius = {
    conversion(temperature)
  }
}

given TemperatureConverter[Celcius, Farenheit] with {
  override def convert[Farenheit](temperature: Celcius)(using conversion: Conversion[Celcius, Farenheit]): Farenheit = {
    conversion(temperature)
  }
}

val celciusConverter = summon[TemperatureConverter[Celcius, Farenheit]]
val temperatureCelcius = Celcius(37.0)
val converted = celciusConverter.convert(Celcius(37.0))

println(s"Temperature in Celcius: ${temperatureCelcius.value}")
println(s"Converted temperature in Farenheit: ${converted.value}")

val farenheitConverter = summon[TemperatureConverter[Farenheit, Celcius]]
val temperatureFaren = Farenheit(102.0)
val convertedTemp = farenheitConverter.convert(temperatureFaren)

println(s"Temperature in Farenheit: ${temperatureFaren.value}")
println(s"Converted temperature in Celcius: ${convertedTemp.value}")