package com.thirtydays.kotlin.ui

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.presenter.view.BaseView
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.thirtydays.kotlin.receiver.BluetoothStateReceiver
import com.thirtydays.kotlin.utils.BluetoothUtils

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/9
 * desc   :
 * </pre>
 */
abstract class BluetoothActivity<out Presenter : BasePresenter<BaseView<Presenter>>> :
    BaseMvpActivity<Presenter>(), BaseView<Presenter>,
    BluetoothStateReceiver.BroadcastReceiverListener {

    var mBtAdapter: BluetoothAdapter? = null
    private lateinit var mBluetoothStateReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {

        mBtAdapter = BluetoothUtils.getBluetoothAdapter(this)
        mBluetoothStateReceiver = BluetoothStateReceiver(this)

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mBluetoothStateReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mBluetoothStateReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTION_REQUEST_ENABLE_BLUETOOTH -> {
                if (resultCode == RESULT_OK) {
                    onBluetoothEnabled()
                } else if (resultCode == RESULT_CANCELED) {
                    onBluetoothDisabled()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    open fun checkEnableBt() {
        if (isBluetoothEnabled()) {
            BluetoothUtils.enableBluetooth(this, ACTION_REQUEST_ENABLE_BLUETOOTH)
        } else {
            onBluetoothEnabled()
        }
    }

    private fun isBluetoothEnabled(): Boolean {
        return mBtAdapter == null || !mBtAdapter!!.isEnabled
    }

    companion object {
        const val ACTION_REQUEST_ENABLE_BLUETOOTH = 0x11

        const val SCANNING_TIME = 10000L
    }

}