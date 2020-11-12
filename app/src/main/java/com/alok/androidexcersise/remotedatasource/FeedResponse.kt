package com.alok.androidexcersise.remotedatasource

import com.alok.androidexcersise.model.Feed
import java.io.Serializable

/**
 * Created by Alok Soni on 12/11/20.
 */
data class FeedResponse( val title: String?, val rows: List<Feed>?) : Serializable