package foo.bar.clean.data.device.netmon

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import co.early.fore.kt.core.coroutine.awaitIO
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.logging.Logger
import foo.bar.clean.domain.services.device.DeviceNetworkChange
import foo.bar.clean.domain.services.device.NetworkMonitorService
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@SuppressLint("MissingPermission")
class AndroidNetworkMonitor(
    private val connectivityManager: ConnectivityManager,
    private val okHttpClient: OkHttpClient,
    application: Application,
    private val logger: Logger,
) : NetworkMonitorService {

    //we start by assuming we have network
    private var networkAvailable = true
    private var networkChangeCallback: DeviceNetworkChange? = null

    init {
        if (Build.VERSION.SDK_INT < 21) {
            logger.i("SDK:${Build.VERSION.SDK_INT} using BroadcastReceiver")
            application.registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        logger.i("CONNECTIVITY_ACTION broadcast update thread:${Thread.currentThread().id}")
                        if (connectivityManager.activeNetworkInfo?.isConnected ?: false) {
                            doubleCheckConnection()
                        } else {
                            networkAvailable = false
                            networkChangeCallback?.onLost()
                        }
                    }
                },
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            doubleCheckConnection()
        } else {
            logger.i("SDK:${Build.VERSION.SDK_INT} using registerNetworkCallback()")
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) { // this always gets called immediately on registering
                        super.onAvailable(network)
                        logger.i("NetworkCallback onAvailable() thread:${Thread.currentThread().id} network:$network")
                        doubleCheckConnection()
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        logger.i("NetworkCallback onLost() thread:${Thread.currentThread().id} network:$network")
                        doubleCheckConnection()
                    }
                }
            )
        }
    }

    private fun doubleCheckConnection() {
        logger.i("doubleCheckConnection()")
        launchMain {
            val success = awaitIO {
                try {
                    @Suppress("BlockingMethodInNonBlockingContext")
                    okHttpClient.newCall(
                        Request.Builder().url("https://google.com").head().build()
                    ).execute().isSuccessful
                } catch (e: IOException) {
                    false
                }
            }
            if (success) {
                logger.i("double check network success")
                networkAvailable = true
                networkChangeCallback?.onAvailable()
            } else {
                logger.i("double check network fail")
                networkAvailable = false
                networkChangeCallback?.onLost()
            }
        }
    }

    fun checkIfDown() {
        logger.i("checkIfDown() networkAvailable:${networkAvailable}")
        if (!networkAvailable) {
            doubleCheckConnection()
        }
    }

    override fun setCallBack(networkChangeCallback: DeviceNetworkChange?) {
        this.networkChangeCallback = networkChangeCallback
    }
}
