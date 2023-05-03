package com.grgcmz.blescanner.model

class ScanResultAdapter {
    companion object {
        fun getbondState(bondState: Int): String {
            when (bondState) {
                10 -> return "Not Bonded"
                11 -> return "Bonding"
                12 -> return "Bonded"
                else -> {
                    return "N/A"
                }
            }
        }
    }


}