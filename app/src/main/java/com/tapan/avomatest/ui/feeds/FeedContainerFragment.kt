package com.tapan.avomatest.ui.feeds


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tapan.avomatest.R
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.databinding.FragmentFeedContainerBinding
import com.tapan.avomatest.showAlert
import com.tapan.avomatest.ui.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedContainerFragment : BaseFragment() {


    private lateinit var binding: FragmentFeedContainerBinding
    private val viewModel by viewModels<FeedContainerViewModel>()
    private val feeds = mutableListOf<Feed>()

    private lateinit var adapter: FragmentStateAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        attachObserver()
        viewModel.getAllFeed()
    }


    private fun attachObserver() {
        handleLoading(viewModel.getFeedState()) {
            if (!it.feeds.isNullOrEmpty()) {
                feeds.clear()
                feeds.addAll(it.feeds.orEmpty())
                adapter.notifyDataSetChanged()
                binding.apply {
                    TabLayoutMediator(
                        tabLayout,
                        viewPagger,
                        false,
                        true
                    ) { tab, position ->
                        val uri = Uri.parse(feeds[position].link)
                        tab.text = uri.host.plus("-").plus(uri.pathSegments.lastOrNull().orEmpty())
                        setToolbarTitle(feeds[position].link)
                    }.attach()
                }
            } else {
                setToolbarTitle(getString(R.string.feed_fragment_label))
                showAlert("Add some feeds !!")
            }
        }
    }

    private fun initUi() {

        adapter =
            object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
                override fun getItemCount(): Int {
                    return feeds.size
                }

                override fun createFragment(position: Int): Fragment {
                    return FeedsFragment.newInstance(feeds[position].link)
                }
            }



        binding.apply {
            viewPagger.adapter = adapter

        }

    }

}