package com.alok.androidexcersise.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alok.androidexcersise.R
import com.alok.androidexcersise.model.Feed
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feed_list_item.view.*

/**
 * Created by Alok Soni on 12/11/20.
 */

class FeedListAdapter(private var feeds: List<Feed>) :
    RecyclerView.Adapter<FeedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list_item, parent, false)
        return MyViewHolder(view)
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

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewTitle: TextView = view.textviewTitle
        private val textViewDesc: TextView = view.textviewDesc
        private val imageView: ImageView = view.imageview
        fun bind(feed: Feed) {
            textViewTitle.text = feed.title
            textViewDesc.text = feed.description
            var imageURL = if(feed.imageHref != null) feed.imageHref.trim() else feed.imageHref
            Glide.with(imageView.context).load(imageURL).into(imageView)
        }
    }
}