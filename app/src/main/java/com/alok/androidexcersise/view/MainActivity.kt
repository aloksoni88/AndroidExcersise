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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        initView()
    }

    override fun onResume() {
        super.onResume()
        feedViewModel.loadFeedList();
    }

    private fun setupViewModel(){
        feedViewModel = ViewModelProvider(this,Injection.getViewModelFactory()).get(FeedViewModel::class.java)
        feedViewModel.feeds.observe(this,onSuccessObserver)
        feedViewModel.onLoading.observe(this,onLoadingObserver)
        feedViewModel.isEmptyList.observe(this,onEmptyObserver)
        feedViewModel.onError.observe(this,onErrorObserver)
    }

    private fun initView(){
        feedListAdapter = FeedListAdapter(feedViewModel.feeds.value?.rows?: emptyList())
        feedListRecyclerView.layoutManager = LinearLayoutManager(this)
        feedListRecyclerView.adapter = feedListAdapter
    }

    private val onSuccessObserver = Observer<FeedResponse>{
        Log.d(TAG, "feed list ${it.rows}" )
        emptyLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        it.rows?.let { it1 -> feedListAdapter.update(it1) }
    }

    private val onLoadingObserver = Observer<Boolean> {
        Log.d(TAG, " Result is getting loaded  $it")
        progressBar.visibility = if(it) View.VISIBLE else View.GONE
    }

    private val onEmptyObserver = Observer<Any> {
        Log.d(TAG, " No result found  $it")
        emptyLayout.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private val onErrorObserver = Observer<Any> {
        Log.d(TAG, " Error while getting feeds  $it")
        errorLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        textViewError.text = "$it"
    }


}