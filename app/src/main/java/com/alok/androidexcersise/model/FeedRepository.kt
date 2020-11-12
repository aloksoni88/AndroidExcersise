package com.alok.androidexcersise.model

import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.remotedatasource.ResultCallback

/**
 * Created by Alok Soni on 12/11/20.
 */

class FeedRepository(private val feedDataSource: FeedDataSource) {

    fun getFeedsList(callback: ResultCallback<FeedResponse>) {
        feedDataSource.getFeeds(callback)
    }

    fun cancel() {
        feedDataSource.cancel()
    }
}