package com.grgcmz.blescanner.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grgcmz.blescanner.R
import com.grgcmz.blescanner.controller.utils.toHex
import com.grgcmz.blescanner.model.DeviceModel
import com.grgcmz.blescanner.model.ScanResultAdapter
import com.grgcmz.blescanner.view.theme.BLEScannerTheme

/**
 * Composable function for rendering a device card.
 *
 * @param deviceModel The device model representing the device.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceCard(deviceModel: DeviceModel) {
    Card(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {

            Column() {

                Row() {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                        contentDescription = "Bluetooth Icon",
                        modifier = Modifier.offset(y = 4.dp)

                    )
                    Text(
                        text = deviceModel.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Center Column
                FlowColumn(
                    modifier = Modifier.fillMaxWidth(),
                    ) {
                    Text(
                        text = deviceModel.address,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${deviceModel.rssi} dBm",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = ScanResultAdapter.getBondState(deviceModel.bondState),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "AD Flags: ${deviceModel.advertiseFlags}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    for (pair in deviceModel.parsedBytes){
                        Text(
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                            text = "${pair.first}\nValue:  ${pair.second}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(
                        text = "RAW Data: ${deviceModel.rawDataBytes.toHex()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BLEScannerTheme() {
        DeviceCard(
            deviceModel = DeviceModel(
                "Test",
                "Test",
                2,
                10,
                1,
                ByteArray(10),
                listOf(Pair("Test", "Test")))
        )
    }
}
