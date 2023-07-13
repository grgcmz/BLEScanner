package com.grgcmz.blescanner.controller

import com.grgcmz.blescanner.controller.utils.decodeHex
import com.grgcmz.blescanner.controller.utils.hexToTemp
import com.grgcmz.blescanner.controller.utils.toHex

/**
 * Class responsible for parsing advertising data received from BLE devices.
 */
class AdvParser (){

    /**
     * Parses the byte array of advertising data and returns a list of key-value pairs representing the parsed data.
     *
     * @param bytes The byte array of advertising data.
     * @param deviceName The name of the device emitting the advertising data.
     * @return A list of key-value pairs representing the parsed advertising data.
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    fun parseBytes(bytes: ByteArray, deviceName: String): List<Pair<String, String>>{
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
                parsedBytes.add(decodeData(type, data, deviceName))
            }

            return parsedBytes
    }

    /**
     * Decodes a specific type of advertising data based on the provided type and data.
     *
     * @param type The type of advertising data to decode.
     * @param data The data to be decoded.
     * @param deviceName The name of the device emitting the advertising data.
     * @return A key-value pair representing the decoded advertising data.
     */
    private fun decodeData(type: String, data: String, deviceName: String): Pair<String, String>{
        return when (type) {
            "FF" -> {
                val decodedData = decodeManSpecData(data, deviceName)
                Pair ("Manufacturer Specific Data", decodedData)
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
                Pair("Security Manager Out of Band Flags", "0x$data")
            "12" ->
                Pair("Peripheral Connection Interval Range", "0x$data")
            "14" ->
                Pair("List of 16-bit Service Solicitation UUIDs", "0x$data")
            "15" ->
                Pair("List of 128-bit Service Solicitation UUIDs", "0x$data")
            "16" ->
                Pair("Service Data - 16-bit UUID ", "0x$data")
            "17" ->
                Pair("Public Target Address", "0x$data")
            "18" ->
                Pair("Random Target Address", "0x$data")
            "19" ->
                Pair("Appearance", "0x$data")
            "1A" ->
                Pair("Advertising Interval", "0x$data")
            "1B" ->
                Pair("LE Bluetooth Device Address", "0x$data")
            "1C" ->
                Pair("LE Role", "0x$data")
            "1D" ->
                Pair("Simple Pairing Hash C-256", "0x$data")
            "1E" ->
                Pair("Simple Pairing Randomizer R-256", "0x$data")
            "1F" ->
                Pair("List of 32-bit Service Solicitation UUIDs", "0x$data")
            "20" ->
                Pair("Service Data - 32-bit UUID", "0x$data")
            "21" ->
                Pair("Service Data - 128-bit UUID", "0x$data")
            "22" ->
                Pair("LE Secure Connections Confirmation Value", "0x$data")
            "23" ->
                Pair("LE Secure Connections Random Value", "0x$data")
            "24" ->
                Pair("URI", "0x$data")
            "25" ->
                Pair("Indoor Positioning", "0x$data")
            "26" ->
                Pair("Transport Discovery Data", "0x$data")
            "27" ->
                Pair("LE Supported Features ", "0x$data")
            "28" ->
                Pair("Channel Map Update Indication ", "0x$data")
            "29" ->
                Pair("PB-ADV", "0x$data")
            "2A" ->
                Pair("Mesh Message", "0x$data")
            "2B" ->
                Pair("Mesh Beacon", "0x$data")
            "2C" ->
                Pair("BIGInfo", "0x$data")
            "2D" ->
                Pair("Broadcast_Code", "0x$data")
            "2E" ->
                Pair("Resolvable Set Identifier ", "0x$data")
            "2F" ->
                Pair("Advertising Interval - long", "0x$data")
            "30" ->
                Pair("Broadcast_Name", "0x$data")
            "31" ->
                Pair("Encrypted Advertising Data", "0x$data")
            "32" ->
                Pair("Periodic Advertising Response Timing Information", "0x$data")
            "34" ->
                Pair("Electronic Shelf Label", "0x$data")
            "3D" ->
                Pair("3D Information Data", "0x$data")

            else -> {
                Pair("Unrecognized Type: $type", data)
            }
        }
    }

    /**
     * Decodes manufacturer-specific data based on the provided data and device name. In case of
     * the name being MS1089, the temperature is calculated from the manufacturer-specific data.
     *
     * @param data The manufacturer-specific data to decode.
     * @param deviceName The name of the device emitting the advertising data.
     * @return The decoded manufacturer-specific data as a string.
     */
    private fun decodeManSpecData(data: String, deviceName: String): String {

        return if (deviceName == "MS1089") {
            val temperature = data.drop(4).hexToTemp()
            "$temperature °C"
        } else {
            "0x$data"
        }
    }
}

