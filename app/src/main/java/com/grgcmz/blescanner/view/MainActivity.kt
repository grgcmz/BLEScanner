package com.grgcmz.blescanner.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import timber.log.Timber
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.grgcmz.blescanner.BuildConfig
import com.grgcmz.blescanner.controller.MultiplePermissionHandler
import com.grgcmz.blescanner.controller.PermissionHandler
import com.grgcmz.blescanner.controller.Scanning
import com.grgcmz.blescanner.view.composables.DeviceList
import com.grgcmz.blescanner.view.composables.ScanButton
import com.grgcmz.blescanner.view.theme.BLEScannerTheme

class MainActivity : ComponentActivity() {

    // lazy load bluetoothAdapter and bluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    // multiple permission handler asks for coarse location for some reason
    // Create Permission Handler
    private val multiplePermissionHandler: MultiplePermissionHandler by lazy { MultiplePermissionHandler(this, this)}
    // Scanning
    private val bluetoothLeScanner: BluetoothLeScanner by lazy { bluetoothAdapter?.bluetoothLeScanner!! }

    //private val leDeviceListAdapter = LeDeviceListAdapter()
    private val scanResults = mutableStateListOf<ScanResult>()

    // Define Scan Settings
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
        .build()

    private val deviceNameToFilter = "bma_280"
    private val scanFilters = ScanFilter.Builder()
        .setDeviceName(deviceNameToFilter)
        .build()

    // Device scan callback.
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("Activity Created...")

        setContent {
            BLEScannerTheme() {
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

    private fun entry() {
        multiplePermissionHandler.checkBlePermissions(bluetoothAdapter)
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScanningScreen() {
        var isScanning: Boolean by remember { mutableStateOf(false) }

        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold (
                //contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar (
                        title = {
                            Text(text = "BLE Devices Nearby")
                        },
//                        navigationIcon = {
//                            IconButton(onClick = {}) {
//                                Icon(Icons.Filled.Menu, "backIcon")
//                            }
//                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Text(
                            text = "BLE Devices Nearby",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .padding(top = 55.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                ) {
                                DeviceList(scanResults)
                            }
                        }
                    }

                    ScanButton(
                        isScanning,
                        onClick = {
                            isScanning = Scanning.scanBleDevices(
                                bluetoothLeScanner = bluetoothLeScanner,
                                //scanFilters = listOf(scanFilters),
                                null,
                                scanSettings = scanSettings,
                                scanCallback = scanCallback,
                                scanning = isScanning)
                        }
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BLEScannerTheme() {
            ScanningScreen()
        }
    }
}