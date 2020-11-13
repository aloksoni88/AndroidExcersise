package com.alok.androidexcersise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.remotedatasource.ResultCallback
import com.alok.androidexcersise.model.FeedRepository
import com.alok.androidexcersise.utils.UtilityMethods

/**
 * Created by Alok Soni on 12/11/20.
 */
class FeedViewModel(private val repository: FeedRepository) : ViewModel() {

    private val feedResponse = MutableLiveData<FeedResponse>()
    val feeds: LiveData<FeedResponse> = feedResponse

    private val isLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean> = isLoading

    private val onResultError = MutableLiveData<Any>()
    val onError: LiveData<Any> = onResultError

    private val isEmpty = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = isEmpty

    private val isNetConnected = MutableLiveData<Boolean>()
    val isInternetConnected: LiveData<Boolean> = isNetConnected


    fun loadFeedList() {
        isLoading.value = true
        if(UtilityMethods.isInternetConnected()){
            isNetConnected.value = true
            repository.getFeedsList(object : ResultCallback<FeedResponse> {
                override fun onError(error: String?) {
                    isLoading.value = false
                    onResultError.value = error
                }

                override fun onSuccess(response: FeedResponse) {
                    isLoading.value = false
                    if (feedResponse == null) {
                        isEmpty.value = true
                    } else {
                        feedResponse.value = response
                    }
                }
            })
        }else{
            isNetConnected.value = false;
            return
        }
    }

}
