package com.alok.androidexcersise.model

import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.remotedatasource.ResultCallback

/**
 * Created by Alok Soni on 12/11/20.
 */

interface FeedDataSource{
    fun getFeeds(callback: ResultCallback<FeedResponse>)
    fun cancel()
}