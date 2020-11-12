package com.alok.androidexcersise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alok.androidexcersise.model.FeedRepository

/**
 * Created by Alok Soni on 12/11/20.
 */

class ViewModelFactory(private val repository: FeedRepository) : ViewModelProvider.Factory {

    override fun <Feed : ViewModel?> create(modelClass: Class<Feed>): Feed {
        return FeedViewModel(repository) as Feed
    }
}