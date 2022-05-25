package com.tapan.avomatest.ui.feeds.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tapan.avomatest.data.model.Story

class StoryDiffUtil : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.link == newItem.link && oldItem.isBookMarked == oldItem.isBookMarked
    }
}