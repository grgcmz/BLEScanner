package com.grgcmz.blescanner.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.grgcmz.blescanner.BuildConfig
import com.grgcmz.blescanner.controller.MultiplePermissionHandler
import com.grgcmz.blescanner.controller.Scanning
import com.grgcmz.blescanner.view.composables.DeviceList
import com.grgcmz.blescanner.view.composables.ScanButton
import com.grgcmz.blescanner.view.theme.BLEScannerTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {

    // lazy load bluetoothAdapter and bluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    // Create Multiple Permission Handler to handle all the required permissions
    private val multiplePermissionHandler: MultiplePermissionHandler by lazy {
        MultiplePermissionHandler(this, this)
    }

    // Scanning
    private val bluetoothLeScanner: BluetoothLeScanner by lazy { bluetoothAdapter?.bluetoothLeScanner!! }

    private val scanResults = mutableStateListOf<ScanResult>()

    // Define Scan Settings
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
        .build()

    private val deviceNameToFilter = "MS1089"
    private val scanFilters = ScanFilter.Builder()
        .setDeviceName(deviceNameToFilter)
        .build()

    // Device scan Callback
    private val scanCallback: ScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                scanResults[indexQuery] = result
            } else {
                with(result.device) {
                    Timber.d("Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
                }
                scanResults.add(result)

            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Timber.e("BLE Scan failed with error code: $errorCode")
        }
    }

    // On create function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("Activity Created...")

        // Set the content to the ble scanner theme starting with the Scanning Screen
        setContent {
            BLEScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScanningScreen()
                }
            }
        }

        Timber.d("Content Set...")

        try {
            entry()
        } catch (e: Exception) {
            Timber.tag(e.toString())
        }
    }


    // Entry point for permission checks
    private fun entry() {
        multiplePermissionHandler.checkBlePermissions(bluetoothAdapter)
    }


    // Scanning Screen Composable
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScanningScreen() {
        var isScanning: Boolean by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            // Scaffold as outermost on screen
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = "BLE Devices Nearby")
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            ) {
                // Order UI Elements in a column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues = it)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        // Box containing a Column with the Devices
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                ) {
                                DeviceList(scanResults)
                            }
                        }
                    }
                    // Bottom Row containing two buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Clear Results Button
                        Button(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 24.dp),
                            onClick = { scanResults.clear() },
                            content = {
                                // TODO() toggle text
                                Text("Clear Results")
                            }
                        )
                        // Start/Stop Scanning Button
                        ScanButton(
                            isScanning,
                            onClick = {
                                isScanning = Scanning.scanBleDevices(
                                    bluetoothLeScanner = bluetoothLeScanner,
                                    //scanFilters = listOf(scanFilters),
                                    null,
                                    scanSettings = scanSettings,
                                    scanCallback = scanCallback,
                                    scanning = isScanning
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Preview Scanning Screen for Emulator
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BLEScannerTheme {
            ScanningScreen()
        }
    }
}