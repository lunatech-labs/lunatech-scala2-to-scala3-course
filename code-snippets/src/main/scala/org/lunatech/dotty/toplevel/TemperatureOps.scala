package org.lunatech.dotty.toplevel

// toplevel definitions accessible in the entire package

sealed trait TemperatureScale {
  def value: String
}
object Celsius extends TemperatureScale {
  def value: String = "Celcuis"
}
object Farenheit extends TemperatureScale {
  def value: String = "Farenheit"
}

case class Temperature(value: Double, scale: TemperatureScale) {
  override def toString: String = s"$value ${scale.value}"
}

// A private toplevel definition is always visible from everywhere in the enclosing package.
private def convertToFarenheit(temperature: Temperature): Temperature = {
  Temperature((temperature.value * 9 / 5) + 32, Farenheit)
}

def convertToCelcius(temperature: Temperature): Temperature = {
  Temperature((temperature.value - 32) * 5 / 9, Celsius)
}
