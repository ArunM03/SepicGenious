package com.sepicgenious

import android.app.Dialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sepicgenious.utils.AlertDialogs
import com.sepicgenious.utils.ConnectivityReceiver

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var broadcastReceiver: ConnectivityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastReceiver = ConnectivityReceiver()


    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            Log.e("Internet", "Disconnected")
            AlertDialogs.AlertDialogWarningClose(
                this@BaseActivity,
                "Internet Connection Not Found!"
            )
        } else {
            Log.e("Internet", "Connected")
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }
}
