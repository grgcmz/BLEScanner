package com.grgcmz.blescanner.controller

import com.grgcmz.blescanner.controller.utils.decodeHex
import com.grgcmz.blescanner.controller.utils.toHex

class AdvParser (){

    @OptIn(ExperimentalUnsignedTypes::class)
    fun parseBytes(bytes: ByteArray): List<Pair<String, String>>{
            var bytesInHex: String = bytes.toHex().drop(2) //convert to hex String and drop 0x

            val parsedBytes = mutableListOf<Pair<String, String>>()

            //val bytesIterator = bytesInHex.iterator()
            while(bytesInHex.isNotEmpty()) {
                val len: Int = bytesInHex.take(2).toInt(16)
                if(len == 0) break //break if length is 0
                bytesInHex = bytesInHex.drop(2)
                val type = bytesInHex.take(2)
                bytesInHex = bytesInHex.drop(2)
                val data = bytesInHex.take(len*2-2) // length is in bytes -> 1 byte == 2 Hex Values
                bytesInHex = bytesInHex.drop(len*2-2)
                parsedBytes.add(decodeData(type, data))
            }

            return parsedBytes
    }

    private fun decodeData(type: String, data: String): Pair<String, String>{
        return when (type) {
            "FF" -> {
                Pair("Manufacturer Specific Data", "0x$data")
                // TODO() fix this stuff
//                val temperature: String = data.drop(4)
//                Pair(
//                    "Manufacturer Specific Data",
//                    calculateTemp(temperature).toString()
//                )
            }
            "01" ->
                Pair("Flags", "0x$data")
            "02" ->
                Pair("Incomplete List of 16-bit Service Class UUIDs", "0x$data")
            "03" ->
                Pair("Complete List of 16-bit Service Class UUIDs", "0x$data")
            "04" ->
                Pair("Incomplete List of 32-bit Service Class UUIDs", "0x$data")
            "05" ->
                Pair("Complete List of 32-bit Service Class UUIDs", "0x$data")
            "06" ->
                Pair("Incomplete List of 128-bit Service Class UUIDs", "0x$data")
            "07" ->
                Pair("Complete List of 128-bit Service Class UUIDs", "0x$data")
            "08" -> {
                Pair("Shortened Local Name", data.decodeHex())
            }
            "09" -> {
                Pair("Complete Local Name", data.decodeHex())
            }
            "0A" ->
                Pair("Tx Power Level", "0x$data")
            "0D" ->
                Pair("Class of Device", "0x$data")
            "0E" ->
                Pair("Simple Pairing Hash C-192", "0x$data")
            "0F" ->
                Pair("Simple Pairing Randomizer R-192 C", "0x$data")
            "10" ->
                Pair("Device ID", "0x$data") // TODO() Check why this is double here https://btprodspecificationrefs.blob.core.windows.net/assigned-numbers/Assigned%20Number%20Types/Assigned_Numbers.pdf
            "11" ->
                Pair("", "0x$data")
            "12" ->
                Pair("", "0x$data")
            "14" ->
                Pair("", "0x$data")
            "15" ->
                Pair("", "0x$data")
            "18" ->
                Pair("", "0x$data")
            "19" ->
                Pair("", "0x$data")
            "1A" ->
                Pair("", "0x$data")
            "1B" ->
                Pair("", "0x$data")
            "1C" ->
                Pair("", "0x$data")
            "1D" ->
                Pair("", "0x$data")
            "1E" ->
                Pair("", "0x$data")
            "1F" ->
                Pair("", "0x$data")
            "20" ->
                Pair("", "0x$data")
            "21" ->
                Pair("", "0x$data")
            "22" ->
                Pair("", "0x$data")
            "23" ->
                Pair("", "0x$data")
            "24" ->
                Pair("", "0x$data")
            "25" ->
                Pair("", "0x$data")
            "26" ->
                Pair("", "0x$data")
            "27" ->
                Pair("", "0x$data")
            "28" ->
                Pair("", "0x$data")
            "29" ->
                Pair("", "0x$data")
            "2A" ->
                Pair("", "0x$data")
            "2B" ->
                Pair("", "0x$data")
            "2C" ->
                Pair("", "0x$data")
            "2D" ->
                Pair("", "0x$data")
            "2E" ->
                Pair("", "0x$data")
            "2F" ->
                Pair("", "0x$data")
            "30" ->
                Pair("", "0x$data")
            "31" ->
                Pair("", "0x$data")
            "32" ->
                Pair("", "0x$data")
            "34" ->
                Pair("", "0x$data")
            "3D" ->
                Pair("", "0x$data")

            else -> {
                Pair("AD Type: $type", data)
            }
        }
    }
}

