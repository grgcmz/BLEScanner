package com.grgcmz.blescanner.controller

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import timber.log.Timber

/**
 * Helper object for scanning BLE devices.
 */
object Scanning {

    /**
     * Starts or stops scanning for BLE devices.
     *
     * @param bluetoothLeScanner The BluetoothLeScanner instance used for scanning.
     * @param scanFilters Optional list of ScanFilters to apply during scanning.
     * @param scanSettings The ScanSettings to use for scanning.
     * @param scanCallback The ScanCallback to receive scan results.
     * @param scanning Whether it is already scanning (true) or not (false).
     * @return True if scanning was started, false if scanning was stopped.
     */
    @SuppressLint("MissingPermission")
    fun scanBleDevices(
        bluetoothLeScanner: BluetoothLeScanner,
        scanFilters: List<ScanFilter>?,
        scanSettings: ScanSettings,
        scanCallback: ScanCallback,
        scanning: Boolean
    ): Boolean {
        return if (!scanning) {
            Timber.d("Starting Scan...")
            bluetoothLeScanner.startScan(
                scanFilters,
                scanSettings,
                scanCallback
            )
            true
        } else {
            Timber.d("Stopping Scan...")
            bluetoothLeScanner.stopScan(scanCallback)
            false
        }
    }
}