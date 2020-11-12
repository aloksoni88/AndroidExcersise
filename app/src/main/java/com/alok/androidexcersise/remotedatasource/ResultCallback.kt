package com.alok.androidexcersise.remotedatasource



/**
 * Created by Alok Soni on 12/11/20.
 */
interface ResultCallback<FeedResponse> {
    fun onSuccess(feedResponse: FeedResponse)
    fun onError(error: String?)
}