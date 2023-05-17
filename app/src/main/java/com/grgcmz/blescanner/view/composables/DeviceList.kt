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
fun DeviceList(result: MutableList<ScanResult>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(result) { result ->
            val deviceModel = DeviceModel(
                result.device.name ?: "Unknown",
                result.device.address ?: "Unknown",
                result.rssi ?: 0,
                result.device.bondState,
                result.scanRecord!!.advertiseFlags,
                result.scanRecord!!.bytes,
                AdvParser().parseBytes(result.scanRecord!!.bytes)
                )
            DeviceCard(deviceModel)
        }
//        items(result) { result ->
//            val deviceModel = DeviceModel(
//                result.device.name,
//                result.device.address,
//                result.rssi,
//                result.device.bondState
//            )
//            ExpandableDeviceCard(
//                deviceModel = deviceModel,
//                onCardArrowClick = {/*TODO*/},
//                expanded = /*TODO*/
//            )
//        }
    }
}
