package com.grgcmz.blescanner.controller

import android.annotation.SuppressLint
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

object Scanning {

    @SuppressLint("MissingPermission")
    fun scanBleDevices(bluetoothLeScanner: BluetoothLeScanner, scanCallback: ScanCallback, scanning: Boolean): Boolean {
        if (!scanning) {
            Timber.d("Starting Scan...")
            bluetoothLeScanner.startScan(scanCallback)
            return true
        } else {
            Timber.d("Stopping Scan...")
            bluetoothLeScanner.stopScan(scanCallback)
            return false
        }
    }
}