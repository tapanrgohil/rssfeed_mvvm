package com.tapan.avomatest.ui.feeds.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.data.model.Story

class FeedDiffUtil : DiffUtil.ItemCallback<Feed>() {
    override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem.link == newItem.link
    }
}