package com.alok.androidexcersise.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*


/**
 * Created by Alok Soni on 12/11/20.
 */

@Suppress("DEPRECATION")
class UtilityMethods{

    companion object{
        fun isInternetConnected(): Boolean {
//        if (!isNetworkConnected(context)) {
//            return false
//        }
            var inetAddress: InetAddress? = null
            try {
                val future: Future<InetAddress?> =
                    Executors.newSingleThreadExecutor().submit(object : Callable<InetAddress?> {
                        override fun call(): InetAddress? {
                            return try {
                                InetAddress.getByName("google.com")
                            } catch (e: UnknownHostException) {
                                null
                            } catch (e: Exception) {
                                null
                            }
                        }
                    })
                inetAddress = future.get(1000, TimeUnit.MILLISECONDS)
                future.cancel(true)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: TimeoutException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return inetAddress != null && !inetAddress.equals("")
        }

        public fun isNetworkConnected(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }
    }
}