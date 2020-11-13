package com.alok.androidexcersise.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alok.androidexcersise.R
import com.alok.androidexcersise.di.Injection
import com.alok.androidexcersise.remotedatasource.FeedResponse
import com.alok.androidexcersise.utils.Constants
import com.alok.androidexcersise.view.adapter.FeedListAdapter
import com.alok.androidexcersise.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*


class MainActivity : AppCompatActivity() {
    companion object{
         val TAG = MainActivity::class.java.simpleName;
    }

    private lateinit var feedViewModel : FeedViewModel
    private lateinit var feedListAdapter: FeedListAdapter
    private var feedResponse: FeedResponse? = null
    private var isPulltoRefresh: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if(feedResponse == null) {
            feedViewModel.loadFeedList();
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(Constants.DATA, feedViewModel.feeds.value)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState != null) {
            feedResponse = savedInstanceState.getSerializable(Constants.DATA) as FeedResponse?
            feedResponse?.rows?.let { it -> feedListAdapter.update(it) }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setToolbar(titleValue : String?){
        titleValue?.let { supportActionBar?.title = titleValue }
    }

    private fun setupViewModel(){
        feedViewModel = ViewModelProvider(this,Injection.getViewModelFactory()).get(FeedViewModel::class.java)
        feedViewModel.feeds.observe(this,onSuccessObserver)
        feedViewModel.onLoading.observe(this,onLoadingObserver)
        feedViewModel.isEmptyList.observe(this,onEmptyObserver)
        feedViewModel.onError.observe(this,onErrorObserver)
        feedViewModel.isInternetConnected.observe(this,noInternetObserver)
    }

    private fun initView(){
        feedListAdapter = FeedListAdapter(feedViewModel.feeds.value?.rows?: emptyList())
        feedListRecyclerView.layoutManager = LinearLayoutManager(this)
        feedListRecyclerView.adapter = feedListAdapter

        swipeContainer.setOnRefreshListener {
            isPulltoRefresh = true
            feedViewModel.loadFeedList()
        }
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
    }

    private val onSuccessObserver = Observer<FeedResponse>{
        Log.d(TAG, "feed list ${it.rows}" )
        emptyLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        swipeContainer.isRefreshing = false
        progressBar.visibility = View.GONE
        setToolbar(it.title)
        feedResponse = it
        it.rows?.let { it1 -> feedListAdapter.update(it1) }
    }

    private val noInternetObserver = Observer<Boolean> {
        Log.d(TAG, " No internet connected  $it")
        progressBar.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        noInternetLayout.visibility = View.VISIBLE
        swipeContainer.isRefreshing = false
    }

    private val onLoadingObserver = Observer<Boolean> {
        Log.d(TAG, " Result is getting loaded  $it")
        val visibility = if(it) View.VISIBLE else View.GONE
        if(visibility == View.VISIBLE){
            progressBar.visibility = if(!isPulltoRefresh)  View.VISIBLE else View.GONE
        }else{
            progressBar.visibility = View.GONE
            swipeContainer.isRefreshing = false
        }
    }

    private val onEmptyObserver = Observer<Any> {
        Log.d(TAG, " No result found  $it")
        emptyLayout.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        swipeContainer.isRefreshing = false
    }

    private val onErrorObserver = Observer<Any> {
        Log.d(TAG, " Error while getting feeds  $it")
        errorLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        swipeContainer.isRefreshing = false
        textViewError.text = "$it"
    }


}