package com.tapan.avomatest.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tapan.avomatest.databinding.LoadingListViewBinding
import com.tapan.avomatest.visible

class CustomSwipeRefresh : SwipeRefreshLayout, LoadingView {

    private lateinit var binding: LoadingListViewBinding

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs) {
        init(attrs, 0)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        binding = LoadingListViewBinding.inflate(LayoutInflater.from(context), this)
    }

    override fun onStartLoading() {
        post {
            isRefreshing = true
        }
    }

    override fun onStopLoading(success: Boolean, message: String) {
        if (!success) {
            Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
        }
        post {
            isRefreshing = false
        }
    }

    override fun onInit() {
        visible()
    }

    fun getViewPager() = binding.viewPagger
}