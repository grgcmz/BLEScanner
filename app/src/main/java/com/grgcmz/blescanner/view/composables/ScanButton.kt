package com.grgcmz.blescanner.view.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScanButton(
    scanning: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(16.dp),
        onClick = onClick,
        content = {
            // TODO() toggle text
            Text(if (scanning) "Stop Scanning" else "Start Scanning")
        }
    )
}