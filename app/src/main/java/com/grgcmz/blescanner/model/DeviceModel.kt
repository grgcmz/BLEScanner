package com.grgcmz.blescanner.model

data class DeviceModel(
    val name: String = "Unknown",
    val address: String = "Unknown",
    val rssi: Int = 0,
    val bondState: Int,
    val advertiseFlags: Int,
    val rawDataBytes: ByteArray,
    val parsedBytes: List<Pair<String, String>>,
) {
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

