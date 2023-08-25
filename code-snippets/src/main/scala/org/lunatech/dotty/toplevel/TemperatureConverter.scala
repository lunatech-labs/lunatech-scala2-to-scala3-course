package org.lunatech.dotty.toplevel

object TemperatureConverter extends App {

  val bodyTempCelcius = Temperature(37, Celsius)

  val bodyTempFaren = convertToFarenheit(bodyTempCelcius)

  println(bodyTempCelcius)
  println(bodyTempFaren)
}
