package com.grgcmz.blescanner.model

import android.bluetooth.BluetoothDevice
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class DeviceListAdapter : BaseAdapter() {
    private val devices: ArrayList<BluetoothDevice> = ArrayList<BluetoothDevice>()

    fun addDevice(device: BluetoothDevice) {
        if (!devices.contains(device)) {
            devices.add(device)
        }
    }

    fun getDevice(position: Int): BluetoothDevice? {
        return devices[position]
    }

    fun clear() {
        devices.clear()
    }

    override fun getCount(): Int {
        return devices.size
    }

    override fun getItem(i: Int): Any {
        return devices[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}