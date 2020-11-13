package com.alok.androidexcersise.di

import androidx.lifecycle.ViewModelProvider
import com.alok.androidexcersise.remotedatasource.FeedRemoteDataSource
import com.alok.androidexcersise.model.FeedDataSource
import com.alok.androidexcersise.model.FeedRepository
import com.alok.androidexcersise.retrofit.ApiClient
import com.alok.androidexcersise.viewmodel.ViewModelFactory

/**
 * Created by Alok Soni on 12/11/20.
 */
object Injection{
    private val feedDataSource: FeedDataSource = FeedRemoteDataSource(ApiClient)
    private val feedRepository = FeedRepository(feedDataSource)
    private val feedViewModelFactory = ViewModelFactory(feedRepository)

    fun getViewModelFactory(): ViewModelProvider.Factory {
        return feedViewModelFactory
    }
}