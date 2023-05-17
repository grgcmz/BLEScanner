package com.grgcmz.blescanner.controller.utils

fun ByteArray.toHex(): String =
    "0x" + joinToString(separator = "") { byte ->
        "%02X".format(byte).uppercase()
    }

fun ByteArray.print(): String =
    joinToString(separator = ",") { byte ->
        byte.toInt().toString()
    }

fun Int.intToHex(): String =
    "%02X".format(this)


// Source: https://stackoverflow.com/questions/71673452/how-to-convert-hex-string-to-ascii-string-in-kotlin
fun String.decodeHex(): String {
    require(length % 2 == 0) {"Invalid Input: Uneven length"}
    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
        .toString(Charsets.UTF_8)
}