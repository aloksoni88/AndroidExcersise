package com.alok.androidexcersise.utils

import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*


/**
 * Created by Alok Soni on 12/11/20.
 */

@Suppress("DEPRECATION")
class UtilityMethods{

    companion object{

        /**
         * Method to check the internet connection.
         */
        fun isInternetConnected(): Boolean {
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
    }
}