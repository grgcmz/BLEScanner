package com.grgcmz.blescanner.controller.utils

/**
 * Converts a ByteArray to its hexadecimal representation.
 *
 * @return The hexadecimal representation of the ByteArray, prefixed with "0x".
 */
fun ByteArray.toHex(): String =
    "0x" + joinToString(separator = "") { byte ->
        "%02X".format(byte).uppercase()
    }

/**
 * Converts a ByteArray to a string representation of its individual byte values.
 *
 * @return A comma-separated string of the byte values.
 */
fun ByteArray.print(): String =
    joinToString(separator = ",") { byte ->
        byte.toInt().toString()
    }


/**
 * Decodes a hexadecimal string to its corresponding ASCII string.
 *
 * @throws IllegalArgumentException if the input string has an uneven length.
 * @return The ASCII string representation of the decoded hexadecimal string.
 */
// Source: https://stackoverflow.com/questions/71673452/how-to-convert-hex-string-to-ascii-string-in-kotlin
fun String.decodeHex(): String {
    require(length % 2 == 0) {"Invalid Input: Uneven length"}
    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
        .toString(Charsets.UTF_8)
}

/**
 * Converts a hexadecimal string to temperature representation.
 *
 * @return The temperature value as a string.
 */
fun String.hexToTemp(): String {
    val uncompensatedTemp = this.toInt(radix = 16)
    val compensatedTemp: Double = uncompensatedTemp.toDouble() / 40.0 - 80.0
    return ((compensatedTemp * 10.0).toLong() / 10.0).toString()
}