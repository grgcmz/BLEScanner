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

@Deprecated("This class is deprecated and it's not needed anymore. Use MultiplePermissionHandler instead, since bluetooth requires multiple permissions in any case. ")
/**
 * [Deprecated] Handler class for handling permissions related to Bluetooth and Location.
 *
 * @property activity The ComponentActivity instance.
 * @property context The Context instance.
 */
class PermissionHandler (private val activity: ComponentActivity, private val context: Context){

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
                Toast.makeText(context, "Bluetooth Scan permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Bluetooth Scan permission denied", Toast.LENGTH_SHORT).show()
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

    /**
     * [Deprecated] Checks the BLE-related permissions and requests them if necessary.
     *
     * @param bluetoothAdapter The BluetoothAdapter instance.
     */
    @Deprecated("This method is deprecated. Use checkBlePermissions() in MultiplePermissionHandler instead.")

    fun checkBlePermissions(bluetoothAdapter: BluetoothAdapter?) {
        Timber.d("Checking permissions...")

        // Request to enable Bluetooth
        if (!bluetoothAdapter!!.isEnabled) {
            Timber.e("Bluetooth not enabled, terminating...")
            Toast.makeText(context, "Please turn on Bluetooth and try again.", Toast.LENGTH_SHORT)
                .show()
            this.activity.finish() // TODO() make this more elegant and don't just crash
        }

        // Request Fine Location Permission
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestFineLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBluetoothScanPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBluetoothConnectPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            Timber.d("Permission check passed...")
        }
    }
}