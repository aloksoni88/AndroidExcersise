package com.alok.androidexcersise.remotedatasource

import android.util.Log
import com.alok.androidexcersise.model.FeedDataSource
import com.alok.androidexcersise.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Alok Soni on 12/11/20.
 */
class FeedRemoteDataSource(apiClient: ApiClient) : FeedDataSource {
    private var call: Call<FeedResponse>? = null
    private val service = apiClient.build()

    override fun getFeeds(callback: ResultCallback<FeedResponse>) {

        call = service?.feeds()
        Log.d("API url ", "getFeeds: ${call?.request()?.url}")
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                response.body()?.let {
                    Log.d("onSuccess", "onResponse: ${response}")
                    if (response.isSuccessful) {
                        Log.d("onResponse", "API success")
                        callback.onSuccess(it)
                    } else {
                        Log.d("onResponse", "API Failed")
                        callback.onError("Error getting feeds")
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.let {
            it.cancel()
        }
    }
}