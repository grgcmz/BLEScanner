package com.grgcmz.blescanner.model

class ScanResultAdapter {
    companion object {
        fun getBondState(bondState: Int): String {
            return when (bondState) {
                10 -> "Not Bonded"
                11 -> "Bonding"
                12 -> "Bonded"
                else -> {
                    "N/A"
                }
            }
        }
    }


}