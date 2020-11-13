package com.alok.androidexcersise.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alok.androidexcersise.R
import com.alok.androidexcersise.databinding.FeedListItemBinding
import com.alok.androidexcersise.model.Feed
import com.bumptech.glide.Glide


/**
 * Created by Alok Soni on 12/11/20.
 */

class FeedListAdapter(private var feeds: List<Feed>) :
    RecyclerView.Adapter<FeedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FeedListItemBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bind(feeds[position])
    }

    override fun getItemCount(): Int {
        return feeds.size
    }

    fun update(data: List<Feed>) {
        feeds = data
        notifyDataSetChanged()
    }


    class MyViewHolder(val binding: FeedListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            binding.feedItem = feed
        }
    }

    companion object {
        val TAG = FeedListAdapter::class.java.simpleName

        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(imageView: ImageView, url: String?) {
            val imageURL = if(url != null) url.trim().replace("http","https") else url
            Log.d(TAG, "imageURL $imageURL")
            Glide.with(imageView.context).load(imageURL).placeholder(R.drawable.ic_image_placeholder).into(imageView)
        }
    }
}