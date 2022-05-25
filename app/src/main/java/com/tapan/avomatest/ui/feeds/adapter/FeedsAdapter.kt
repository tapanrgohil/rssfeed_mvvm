package com.tapan.avomatest.ui.feeds.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.databinding.ItemFeedBinding

class FeedAdapter(private val callBack: (Feed) -> Unit) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private val asyncDiffUtil = AsyncListDiffer(this, FeedDiffUtil())

    class FeedViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rssItem: Feed?, callBack: (Feed) -> Unit) {
            binding.ivDelete.setOnClickListener {
                rssItem?.let { it1 -> callBack.invoke(it1) }
            }

            binding.tvTitle.text = rssItem?.link.orEmpty()
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeedAdapter.FeedViewHolder(ItemFeedBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(asyncDiffUtil.currentList[position], callBack)
    }

    override fun getItemCount(): Int = asyncDiffUtil.currentList.size
    fun submitList(list: List<Feed>) {
        asyncDiffUtil.submitList(list)
    }


}