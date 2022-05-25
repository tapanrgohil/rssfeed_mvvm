package com.tapan.avomatest.ui.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.databinding.FragmentFirstBinding
import com.tapan.avomatest.ui.core.BaseFragment
import com.tapan.avomatest.ui.feeds.adapter.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FeedsFragment : BaseFragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val link: String?
        get() {
            return arguments?.getString("link", "")

        }

    companion object {
        fun newInstance(link: String): FeedsFragment {
            val args = Bundle()
            args.putString("link", link)
            val fragment = FeedsFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private val feedAdapter = StoryAdapter({ story: Story ->
        viewModel.addRemoveBookMarkStory(story)
    }) { story: Story ->
        parentFragment?.findNavController()
            ?.navigate(
                FeedContainerFragmentDirections.actionFeedContainerFragmentToStoryDetailsFragment(
                    story
                )
            )
    }

    private val viewModel by viewModels<FeedsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        attachObserver()
    }

    private fun attachObserver() {
        handleLoading(viewModel.getStoryState(), binding.swipeRefresh) {
            it.feeds?.let {
                feedAdapter.submitList(it)
          
            }
        }
    }

    private fun initUI() {
        binding.swipeRefresh.getViewPager().apply {
            adapter = feedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL

        }
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun refresh() {
        link?.let { viewModel.getFeeds(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}