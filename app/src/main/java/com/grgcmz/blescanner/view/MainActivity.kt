package com.grgcmz.blescanner.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Surface
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import timber.log.Timber
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grgcmz.blescanner.BuildConfig
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

    // Create Permission Handler
    private val permissionHandler: PermissionHandler = PermissionHandler(this, this)

    // Scanning
    private val bluetoothLeScanner: BluetoothLeScanner by lazy { bluetoothAdapter?.bluetoothLeScanner!! }

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    //private val leDeviceListAdapter = LeDeviceListAdapter()
    private val scanResults = mutableStateListOf<ScanResult>()

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
                Timber.d(scanResults.toString())
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Timber.e("BLE Scan failed with error code: $errorCode")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        entry()

    }

    private fun entry() {
        permissionHandler.checkBlePermissions(bluetoothAdapter)
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
                        navigationIcon = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Filled.Menu, "backIcon")
                            }
                        },
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
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DeviceList(scanResults)
                        }
                    }

                    ScanButton(
                        isScanning,
                        onClick = {
                            isScanning = Scanning.scanBleDevices(bluetoothLeScanner, scanCallback, isScanning)
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