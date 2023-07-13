package com.grgcmz.blescanner.model

/**
 * Adapter class for managing scan results.
 */
class ScanResultAdapter {
    companion object {

        /**
         * Returns the string representation of the bond state based on the provided bond state value.
         *
         * @param bondState The bond state value.
         * @return The string representation of the bond state.
         */
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