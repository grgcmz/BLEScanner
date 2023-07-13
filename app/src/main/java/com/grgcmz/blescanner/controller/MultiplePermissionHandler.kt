package com.grgcmz.blescanner.controller

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Handler class for handling multiple permissions related to Bluetooth and Location.
 *
 * @property activity The ComponentActivity instance.
 * @property context The Context instance.
 */
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

    private val requestBluetoothConnectPermissionLauncher by lazy(LazyThreadSafetyMode.NONE) {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(context, "Bluetooth Connect permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Bluetooth Connect permission denied", Toast.LENGTH_SHORT)
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

    /**
     * Checks the BLE-related permissions and requests them if necessary.
     *
     * @param bluetoothAdapter The BluetoothAdapter instance.
     */

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

        val permissionsStateConnect = if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
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

        val permissionsState: String = permissionsStateConnect + permissionsStateScan + permissionsStateLocation

        Timber.d("PermissionsState is $permissionsState")
        // _ _ _ -> FINE_LOCATION, BLUETOOTH_SCAN, BLUETOOTH_CONNECT
        when (permissionsState) {
            "000" // No permission granted
                -> multiplePermissionResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                )
            )
            "001" // Location already granted
                -> multiplePermissionResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                    )
                )
            "010" // Location already granted
            -> multiplePermissionResultLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
            "011" // Location already granted
            -> requestBluetoothConnectPermissionLauncher.launch(
                Manifest.permission.BLUETOOTH_CONNECT
            )
            "100" // Connect already granted
                -> multiplePermissionResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN
                )
            )
            "101" // Connect and Location already granted
            -> requestBluetoothScanPermissionLauncher.launch(
                Manifest.permission.BLUETOOTH_SCAN
            )
            "110" // Connect and Scan already granted
            -> requestFineLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            else -> {
                //all perms already granted
                Timber.d("Permission check passed...")
            }
        }
    }
}
