package com.tapan.avomatest.data.story


import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.tapan.avomatest.MainActivity
import com.tapan.avomatest.databinding.FragmentStoryDetailsBinding
import com.tapan.avomatest.load
import com.tapan.avomatest.ui.custom.GlideImageGetter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StoryDetailsFragment : Fragment(com.tapan.avomatest.R.layout.fragment_story_details) {


    private lateinit var binding: FragmentStoryDetailsBinding
    private val viewModel by viewModels<StoryDetailsViewModel>()

    private val extras by navArgs<StoryDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStoryDetailsBinding.bind(view)

        initUi()
        attachObserver()
    }


    private fun attachObserver() {

    }

    private fun initUi() {
        binding.apply {
            tvTitle.text = extras.story.title
            val image = extras.story.image
            if (image != null) {
                binding.ivImage.load(image)

            } else {

            }
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {


                val text = Html.fromHtml(
                    extras.story.description,
                    Html.FROM_HTML_MODE_LEGACY,
                    GlideImageGetter(requireContext(), binding.tvDescription),
                    null
                )
                binding.tvDescription.text = text
                binding.tvDescription.movementMethod = LinkMovementMethod.getInstance();

            }

        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)
            ?.supportActionBar?.title = extras.story.title
    }

}