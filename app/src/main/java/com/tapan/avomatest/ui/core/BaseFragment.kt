package com.tapan.avomatest.ui.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.tapadoo.alerter.Alerter
import com.tapan.avomatest.MainActivity
import com.tapan.avomatest.gone
import com.tapan.avomatest.showAlert
import com.tapan.avomatest.ui.custom.LoadingView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment() : Fragment() {

    private fun handleError(state: BaseState<*>) {
        Alerter.hide()
        state.error?.let {
            showAlert(it)
        }
    }

    fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
        liveData.observe(this, androidx.lifecycle.Observer(body))

    fun <T : Any, L : LiveData<ViewState<T>>> LifecycleOwner.observeViewState(
        liveData: L,
        loader: View? = null,
        body: (T?) -> Unit
    ) {
        this.observe(liveData) {
            when (it) {
                is ViewState.Loading -> {
                    loader?.gone()
                }
                is ViewState.Error -> {
                    loader?.gone()
                }
                is ViewState.Success -> {
                    body.invoke(it.value)
                    loader?.gone()
                }
            }
        }
    }

    sealed class ViewState<T>(
        val value: T? = null,
        val message: String? = null
    ) {
        class Success<T>(data: T) : ViewState<T>(data)
        class Error<T>(message: String?, data: T? = null) : ViewState<T>(data, message)
        class Loading<T> : ViewState<T>()
    }

    fun <T : BaseState<*>> handleLoading(
        state: StateFlow<T>,
        progress: LoadingView? = null,
        errorCallBack: ((T) -> Unit)? = null,
        callBack: (T) -> Unit
    ): Job {
        if (state is BaseState<*>) {
            state.clearError()
        }
        return viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                state.collectLatest { value ->
                    if (value.isLoading) {
                        if (progress == null) {
//                            showProgressAlert()
                        } else {
                            progress.onStartLoading()
                        }
                    } else {
                        if (value.error == null) {
                            callBack.invoke(value)
                        }
                        if (progress == null) {
//                           Alerter.clearCurrent(requireActivity())
                            if (errorCallBack != null) {
                                errorCallBack.invoke(state.value)
                            } else {
                                handleError(state.value)
                            }
                        } else {
                            progress.onStopLoading(state.value.error == null)
                            handleError(state.value)
                        }
                    }

                }
            }
        }

    }


    fun setToolbarTitle(title: String) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            (requireActivity() as? MainActivity)
                ?.supportActionBar?.title = title

        }
    }
}