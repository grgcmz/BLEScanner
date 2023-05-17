package com.grgcmz.blescanner.controller

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.ContextCompat
import timber.log.Timber

class MultiplePermissionHandler(
    private val activity: ComponentActivity,
    private val context: Context
) {

    // Activity Result API call to check if bluetooth is enabled
    private val requestEnableBtLauncher by lazy(LazyThreadSafetyMode.NONE) {
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Bluetooth not enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Activity Result API call for Fine Location permission
    private val requestFineLocationPermissionLauncher by lazy(LazyThreadSafetyMode.NONE) {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val requestBluetoothScanPermissionLauncher by lazy(LazyThreadSafetyMode.NONE) {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(context, "Bluetooth Scan permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Bluetooth Scan permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val multiplePermissionResultLauncher by lazy(LazyThreadSafetyMode.NONE) {
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle result of permission request
            permissions.entries.forEach { entry ->
                val permission = entry.key
                val granted = entry.value
                if (granted) {
                    Toast.makeText(context, "$permission permission granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "$permission is required. Please grant this permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun checkBlePermissions(bluetoothAdapter: BluetoothAdapter?) {
        Timber.d("Checking permissions...")

        // Request to enable Bluetooth
        if (!bluetoothAdapter!!.isEnabled) {
            Timber.e("Bluetooth not enabled, terminating...")
            Toast.makeText(context, "Please turn on Bluetooth and try again.", Toast.LENGTH_SHORT)
                .show()
            this.activity.finish() // TODO() make this more elegant and don't just crash
        }

        val permissionsStateScan: String = if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "0"
        } else "1"

        val permissionsStateLocation = if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "0"
        } else "1"

        val permissionsState: String = permissionsStateScan + permissionsStateLocation

        Timber.d("PermissionsState is $permissionsState")
        // _ _ _ -> FINE_LOCATION, BLUETOOTH_SCAN, BLUETOOTH_CONNECT
        when (permissionsState) {
            "00" // Connect already granted
            -> multiplePermissionResultLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
            "01" // Connect and Location already granted
            -> requestBluetoothScanPermissionLauncher.launch(
                Manifest.permission.BLUETOOTH_SCAN
            )
            "10" // Connect and Scan already granted
            -> requestFineLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            else -> {
                //all perms already granted
                Timber.d("Permission check passed...")
            }
        }
    }
}
