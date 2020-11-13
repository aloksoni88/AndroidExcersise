package com.alok.androidexcersise

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Alok Soni on 13/11/20.
 */
object ResponseFileReader {
    fun readResponseFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext as AndroidExcersiseApp).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
                Log.d("line", "readResponseFromFile: $it")
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}