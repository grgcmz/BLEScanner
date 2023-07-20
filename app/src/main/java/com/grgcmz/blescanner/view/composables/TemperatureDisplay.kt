package com.grgcmz.blescanner.view.composables

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grgcmz.blescanner.controller.AdvParser
import com.grgcmz.blescanner.model.DeviceModel



    @SuppressLint("MissingPermission")
    @Composable
    fun TemperatureDisplay(result: MutableList<ScanResult>) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(result) { result ->
                val deviceModel = DeviceModel(
                    name = result.device.name ?: "Unknown",
                    address = result.device.address ?: "Unknown",
                    rssi = result.rssi,
                    bondState = result.device.bondState,
                    advertiseFlags = result.scanRecord!!.advertiseFlags,
                    rawDataBytes = result.scanRecord!!.bytes,
                    parsedBytes = AdvParser().parseBytes(2, result.scanRecord!!.bytes, result.device.name ?: "Unknown"))
                TemperatureCard(deviceModel = deviceModel)
            }
        }
    }