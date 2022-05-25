package com.tapan.avomatest.ui.feeds.adapter

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.tapan.avomatest.R
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.databinding.ItemStoryBinding
import com.tapan.avomatest.load


class StoryAdapter(
    private val bookmarkCallBack: (Story) -> Unit,
    val clickCallBack: (Story) -> Unit
) :
    RecyclerView.Adapter<StoryAdapter.FeedViewHolder>() {

    private val asyncDiffUtil = AsyncListDiffer(this, StoryDiffUtil())

    class FeedViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rssItem: Story?, callBack: (Story) -> Unit, clickCallBack: (Story) -> Unit) {
            rssItem?.apply {
                binding.ivFeed.load(rssItem.parseImage()) {
                    setTextColor(it)

                }
                title?.let {
                    binding.tvTitle.text = it
                }
                if (rssItem.isBookMarked) {
                    binding.ivBookMark.setImageResource(R.drawable.ic_baseline_bookmarks_24)
                } else {
                    binding.ivBookMark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                }
                binding.ivBookMark.setOnClickListener {
                    if (!rssItem.isBookMarked) {
                        binding.ivBookMark.setImageResource(R.drawable.ic_baseline_bookmarks_24)
                    } else {
                        binding.ivBookMark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    }
                    callBack.invoke(rssItem)
                }

                itemView.setOnClickListener {
                    clickCallBack.invoke(rssItem)
                }
            }

        }

        private fun setTextColor(bitmap: Bitmap?) {
            if (bitmap != null) {
                Palette.from(bitmap).generate {
                    it?.vibrantSwatch?.let {
                        binding.ivBackground.setBackgroundColor(it.rgb)
                    }
                    it?.vibrantSwatch?.bodyTextColor?.let {
                        binding.tvTitle.setTextColor(it)
                    }
                    it?.vibrantSwatch?.bodyTextColor?.let {
                        binding.ivBookMark.imageTintList = ColorStateList.valueOf(it)
                    }

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StoryAdapter.FeedViewHolder(ItemStoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(asyncDiffUtil.currentList[position], bookmarkCallBack, clickCallBack)
    }

    override fun getItemCount(): Int = asyncDiffUtil.currentList.size
    fun submitList(list: List<Story>) {
        asyncDiffUtil.submitList(list)
    }


}