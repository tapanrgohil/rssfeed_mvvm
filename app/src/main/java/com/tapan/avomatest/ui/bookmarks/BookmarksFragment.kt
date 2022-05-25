package com.tapan.avomatest.ui.bookmarks


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.databinding.FragmentBookmarksBinding
import com.tapan.avomatest.showAlert
import com.tapan.avomatest.ui.core.BaseFragment
import com.tapan.avomatest.ui.feeds.adapter.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : BaseFragment() {

    private lateinit var binding: FragmentBookmarksBinding
    private val viewModel by viewModels<BookmarksViewModel>()

    private val storyAdapter = StoryAdapter({ story: Story ->
        viewModel.removeBookMark(story)
    }, { story: Story ->
        findNavController()
            .navigate(
                BookmarksFragmentDirections.actionBookmarksFragmentToStoryDetailsFragment(
                    story
                )
            )
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        attachObserver()
        viewModel.getAllStories()
    }


    private fun attachObserver() {
        handleLoading(viewModel.getStoryState()) {
            if (it.feeds.isNullOrEmpty()) {
                storyAdapter.submitList(arrayListOf())
                showAlert("Bookmarks is empty!")
            } else {
                storyAdapter.submitList(it.feeds)
            }
        }
    }

    private fun initUi() {
        binding.apply {
            rvBookMark.apply {
                adapter = storyAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

    }

}