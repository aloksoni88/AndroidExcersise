package com.alok.androidexcersise.retrofit

import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Alok Soni on 12/11/20.
 */

object ApiClient{
    private var servicesApiInterface: ServicesApiInterface? = null

    fun build(): ServicesApiInterface? {
        var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())

        var retrofit: Retrofit = builder.client(httpClient.build()).build()
        servicesApiInterface = retrofit.create(
            ServicesApiInterface::class.java
        )

        return servicesApiInterface as ServicesApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    /**
     * Retrofit webservice api client which will have web service api methods
     */
    interface ServicesApiInterface {
        @GET("/s/2iodh4vg0eortkl/facts.json")
        fun feeds(): Call<FeedResponse>
    }
}