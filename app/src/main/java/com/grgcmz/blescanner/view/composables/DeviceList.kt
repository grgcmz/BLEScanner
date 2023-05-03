package com.grgcmz.blescanner.view.composables

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grgcmz.blescanner.model.ScanResultAdapter

@SuppressLint("MissingPermission")
@Composable
fun DeviceList(result: MutableList<ScanResult>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(result) { result ->
            DeviceCard(
                name = result.device.name ?: "Unknown",
                address = result.device.address ?: "Unknown",
                rssi = result.rssi ?: 0,
                bondState = ScanResultAdapter.getbondState(result.device.bondState))
        }
    }
}
