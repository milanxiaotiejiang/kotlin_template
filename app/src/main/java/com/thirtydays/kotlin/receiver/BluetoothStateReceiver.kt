package com.thirtydays.kotlin.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateReceiver(private val mListener: BroadcastReceiverListener) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(
                BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR
            )
            if (state == BluetoothAdapter.STATE_OFF) {
                mListener.onBluetoothDisabled()
            } else if (state == BluetoothAdapter.STATE_ON) {
                mListener.onBluetoothEnabled()
            }
        }
    }

    interface BroadcastReceiverListener {
        fun onBluetoothDisabled()
        fun onBluetoothEnabled()
    }

}