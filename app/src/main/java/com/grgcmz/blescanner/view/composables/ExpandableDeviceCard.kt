package com.grgcmz.blescanner.view.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ContentAlpha
import com.grgcmz.blescanner.R
import com.grgcmz.blescanner.controller.utils.toHex
import com.grgcmz.blescanner.model.DeviceModel
import com.grgcmz.blescanner.model.ScanResultAdapter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpandableDeviceCard(
    deviceModel: DeviceModel
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {
            expandedState = !expandedState //change
        }
    ) {
        // Row with Data on the left and RSSI on the Right
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)

        ) {
            // Data on the left
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                // Title with Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    //horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                        contentDescription = "Bluetooth Icon"

                    )
                    Text(
                        text = deviceModel.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Address and Bonding State
                FlowColumn {

                    Text(
                        text = deviceModel.address,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = ScanResultAdapter.getBondState(deviceModel.bondState),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // RSSI Value in the right 30%
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_signal_cellular_alt_24),
                    contentDescription = "Bluetooth Icon",
                    modifier =
                    Modifier
                        .size(16.dp)
                )
                Text(
                    text = "${deviceModel.rssi} dBm",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (expandedState) {
            FlowColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            ) {
                for (pair in deviceModel.parsedBytes) {
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
        Box {
            // expand arrow
            IconButton(
                modifier = Modifier
                    .alpha(ContentAlpha.medium) // probably remove
                    .rotate(rotationState)
                    .fillMaxWidth()
                    .height(20.dp),
                onClick = {
                    expandedState = !expandedState
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop-Down Arrow"
                )
            }
        }
    }
}

@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableDeviceCard(
        deviceModel = DeviceModel(
            "Test",
            "Test",
            -2,
            10,
            1,
            ByteArray(10),
            listOf(Pair("Manufacturer Specific Data", "0xabcdef"), Pair("Another", "0x123456"))
        )
    )
}