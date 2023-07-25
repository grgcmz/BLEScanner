package com.grgcmz.blescanner.model

/**
 * Represents a device with its properties.
 *
 * @property name The name of the device. Default value is "Unknown".
 * @property address The address of the device. Default value is "Unknown".
 * @property rssi The RSSI (Received Signal Strength Indication) of the device. Default value is 0.
 * @property bondState The bond state of the device.
 * @property advertiseFlags The advertise flags of the device.
 * @property rawDataBytes The raw data bytes of the device.
 * @property parsedBytes The parsed bytes of the device as a list of pairs, where each pair represents a key-value pair.
 */
data class DeviceModel(
    val name: String = "Unknown",
    val address: String = "Unknown",
    val rssi: Int = 0,
    val bondState: Int,
    val advertiseFlags: Int,
    val rawDataBytes: ByteArray,
    val parsedBytes: List<Pair<String, String>>,
) {
    /**
     * Checks if this [DeviceModel] is equal to another object.
     *
     * @param other The other object to compare.
     * @return `false` if the objects are not equal, `true` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceModel

        if (name != other.name) return false
        if (address != other.address) return false
        if (rssi != other.rssi) return false
        if (bondState != other.bondState) return false
        if (advertiseFlags != other.advertiseFlags) return false
        if (!rawDataBytes.contentEquals(other.rawDataBytes)) return false
        if (parsedBytes != other.parsedBytes) return false

        return true
    }

    /**
     * Generates a hash code for this DeviceModel.
     *
     * @return The hash code value.
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + rssi
        result = 31 * result + bondState
        result = 31 * result + advertiseFlags
        result = 31 * result + rawDataBytes.contentHashCode()
        result = 31 * result + parsedBytes.hashCode()
        return result
    }
}

