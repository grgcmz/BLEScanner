package com.grgcmz.blescanner.controller

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import timber.log.Timber

object Scanning {

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