package org.lunatech.dotty.exportclause

object Ovens {
  private class HotAirOven {
    var on = false
    var tempSetting = 180
    def setTemp(temp: Int): Unit = {tempSetting = temp; println(s"Setting temperature to $temp")}
    def turnOn: Unit = on = true
    def turnOff: Unit = on = false
    def status: String = s"Hot-air oven is ${if (on) "on" else "off"} - Temperature setting at $tempSetting"
  }

  private class MicrowaveOven {
    var on = false
    var powerSetting = 600
    def setPower(watts: Int): Unit = {powerSetting = watts; println(s"Setting power to $powerSetting")}
    def turnOn: Unit = on = true
    def turnOff: Unit = on = false
    def status: String = s"Microwave oven is ${if (on) "on" else "off"} - Power setting at $powerSetting Watt"
  }

  class CombiOven {
    private val hotAirOven = new HotAirOven
    private val microwaveOven = new MicrowaveOven
    def status = s"${hotAirOven.status}\n${microwaveOven.status}"

    export hotAirOven.{setTemp, turnOn => hotAirOn, turnOff => hotAirOff}
    export microwaveOven.{setPower, turnOn => microwaveOn, turnOff => microwaveOff}
  }
}
