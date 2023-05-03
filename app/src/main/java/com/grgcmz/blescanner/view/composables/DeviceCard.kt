package com.grgcmz.blescanner.view.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grgcmz.blescanner.R
import com.grgcmz.blescanner.view.MainActivity
import com.grgcmz.blescanner.view.theme.BLEScannerTheme

// TODO () Fix card sizing not extending to edge of screen - 16.dp
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeviceCard(name: String, address: String, rssi: Int, bondState: String) {
    Card(
        modifier = Modifier
            .padding(10.dp)
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
            // Left Icon column
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(0.15f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                    contentDescription = "Bluetooth Icon"
                )
            }

            Column() {

                Column() {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Center Column
                FlowColumn(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachColumn = 3,

                    ) {
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "$rssi dBm",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = bondState,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            // Right connection Button
            // TODO()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BLEScannerTheme() {
        DeviceCard("Test", "Test", 2, "Test")
    }
}
