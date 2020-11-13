package com.alok.androidexcersise

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alok.androidexcersise.model.Feed
import com.alok.androidexcersise.model.FeedDataSource
import com.alok.androidexcersise.model.FeedRepository
import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.remotedatasource.ResultCallback
import com.alok.androidexcersise.viewmodel.FeedViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FeedUnitTest {

    @Mock
    private lateinit var feedDataSource: FeedDataSource

    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var resultCallback: ArgumentCaptor<ResultCallback<FeedResponse>>

    private lateinit var viewModel: FeedViewModel
    private lateinit var repository: FeedRepository

    private lateinit var onLoadingObserver: Observer<Boolean>
    private lateinit var onErrorObserver: Observer<Any>
    private lateinit var isEmptyListObserver: Observer<Boolean>
    private lateinit var onSuccessFeedListObserver: Observer<FeedResponse>

    private lateinit var feedResponse: FeedResponse

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(context.applicationContext).thenReturn(context)

        repository = FeedRepository(feedDataSource)
        viewModel = FeedViewModel(repository)

        createFeedResponse()
        setupObservers()
    }

    @Test
    fun `get feeds with ViewModel and Repository returns all feeds`() {
        with(viewModel) {
            loadFeedList()
            onLoading.observeForever(onLoadingObserver)
            feeds.observeForever(onSuccessFeedListObserver)
        }

        Mockito.verify(feedDataSource, Mockito.times(1)).getFeeds(capture(resultCallback))
        resultCallback.value.onSuccess(feedResponse)

        Assert.assertNotNull(viewModel.onLoading.value)
        Assert.assertTrue(viewModel.feeds.value?.rows?.size == 3)
    }

    @Test
    fun `get feeds with ViewModel and Repository returns an error`() {
        with(viewModel) {
            loadFeedList()
            onLoading.observeForever(onLoadingObserver)
            onError.observeForever(onErrorObserver)
        }
        Mockito.verify(feedDataSource, Mockito.times(1)).getFeeds(capture(resultCallback))
        resultCallback.value.onError("An error while getting feeds")
        Assert.assertNotNull(viewModel.onLoading.value)
        Assert.assertNotNull(viewModel.onError.value)
    }

    private fun setupObservers() {
        onLoadingObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        onErrorObserver = Mockito.mock(Observer::class.java) as Observer<Any>
        isEmptyListObserver = Mockito.mock(Observer::class.java) as Observer<Boolean>
        onSuccessFeedListObserver = Mockito.mock(Observer::class.java) as Observer<FeedResponse>
    }

    private fun createFeedResponse() {
        val feedList: MutableList<Feed> = mutableListOf()
        feedList.add(
            Feed(
                "Title 1",
                "This is title 1 Description",
                "https://fyimusic.ca/wp-content/uploads/2008/06/hockey-night-in-canada.thumbnail.jpg"
            )
        )
        feedList.add(Feed("Title 2", "This is title 2 Description", "https://images.findicons.com/files/icons/662/world_flag/128/flag_of_canada.png"))
        feedList.add(Feed("Title 2", "This is title 2 Description", "https://www.donegalhimalayans.com/images/That%20fish%20was%20this%20big.jpg"))

        feedResponse = FeedResponse("My Title", feedList)
    }
}