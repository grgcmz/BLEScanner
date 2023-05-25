package com.grgcmz.blescanner.controller.utils

/**
 * Calculates the temperature for the MS1089 Temperature Sensor by Microdul.
 * Read more [here](https://www.microdul.com/en/ultra-low-power-sensors/temperatursensor/).
 *
 * @param tBinary the temperature measured by the device as a binary string.
 * @param format Optional argument to calculate Temperature as Fahrenheit. Defaults to Celsius.
 * @return returns the temperature in Celsius or Fahrenheit.
 */
fun calculateTemp(tBinary: String, format: String = "C"): Double {
    // TODO(): Maybe change this to use hex in case the number is returned as hex

    val tDecimal = tBinary.toInt(2) // convert binary string to decimal
    return when (format) {
        "F" ->
            (((tDecimal.toDouble()/40) -80) * 1.8 + 32)
        else -> ((tDecimal.toDouble()/40) - 80)

    }
}