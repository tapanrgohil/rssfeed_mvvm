package com.tapan.avomatest.ui.feeds

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.databinding.DialogNewFeedBinding
import com.tapan.avomatest.databinding.FragmentFeedListBinding
import com.tapan.avomatest.ui.core.BaseFragment
import com.tapan.avomatest.ui.feeds.adapter.FeedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedListFragment : BaseFragment() {

    private lateinit var binding: FragmentFeedListBinding
    private val viewModel: FeedListViewModel by viewModels<FeedListViewModel>()

    private val feedAdapter = FeedAdapter { feed: Feed ->
        viewModel.removeFeed(feed.link)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        attachObserver()
        viewModel.getAllFeed()
    }

    private fun attachObserver() {
        handleLoading(viewModel.getFeedState()) {
            feedAdapter.submitList(it.feeds.orEmpty())
        }
    }

    private fun init() {
        binding.rvFeeds.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context)
        }


        binding.fab.setOnClickListener {
            showAddFeedDialog()
        }
    }

    private fun showAddFeedDialog() {

        val dialogBinding = DialogNewFeedBinding.inflate(LayoutInflater.from(requireContext()))
        Dialog(requireContext())
            .apply {
                setContentView(dialogBinding.root)
                dialogBinding.btAdd.setOnClickListener {
                    if (android.util.Patterns.WEB_URL.matcher(dialogBinding.etFeed.text.toString()).matches()
                    ) {
                        viewModel.addFeed(dialogBinding.etFeed.text.toString())
                        dismiss()
                    } else {
                        dialogBinding.etFeed.error = "Invalid URL"
                    }
                }
                show()
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            }
    }
}