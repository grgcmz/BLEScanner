package com.grgcmz.blescanner.view.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A Composable function that displays a button to start or stop scanning.
 *
 * @param scanning Whether scanning is currently active or not.
 * @param onClick Callback function to be executed when the button is clicked.
 */
@Composable
fun ScanButton(
    scanning: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 24.dp),
        onClick = onClick,
        content = {
            Text(if (scanning) "Stop Scanning" else "Start Scanning")
        }
    )
}