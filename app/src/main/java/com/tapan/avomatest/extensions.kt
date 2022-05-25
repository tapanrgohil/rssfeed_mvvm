package com.tapan.avomatest

import android.graphics.Bitmap
import android.view.View
import android.view.View.*
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.tapadoo.alerter.Alerter
import com.tapan.avomatest.data.base.Resource
import com.tapan.avomatest.data.base.Status
import com.tapan.avomatest.ui.core.BaseState
import com.tapan.avomatest.ui.custom.LoadingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


fun View.gone() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.visible() {
    visibility = VISIBLE
}

fun Fragment.showAlert(message: String, title: String? = null, callback: (() -> Unit)? = null) {
    val alert = Alerter.create(requireActivity())
        .setText(message)
        .setBackgroundColorRes(R.color.black)
        .setTitle(title.orEmpty())
        .enableInfiniteDuration(false)
        .enableSwipeToDismiss()

    if (callback != null) {
        alert.setOnHideListener { callback.invoke() }
    }
    alert.show()
}

fun Fragment.showProgressAlert(
    @StringRes message: Int = R.string.please_wait,
    title: String? = null,
    callback: (() -> Unit)? = null
): Alerter {
    val alert = Alerter.create(requireActivity())
        .setText(message)
        .enableProgress(true)
        .setBackgroundColorRes(R.color.black)
        .setTitle(title.orEmpty())
        .enableInfiniteDuration(false)
        .disableOutsideTouch()

    if (callback != null) {
        alert.setOnHideListener { callback.invoke() }
    }

    alert.show()

    return alert

}

fun Any.toJson() = Gson().toJson(this)
inline fun <reified T> String.fromJson(): T? = Gson().fromJson(this, T::class.java);

fun Date.toYYTFormat(): String {
    val dateTimeFormatter = SimpleDateFormat("dd MMM yyyy")
    return dateTimeFormatter.format(this)
}

fun String.fromDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.parse(this)
}

fun <R> Fragment.handleDataResponse(
    stateFlow: StateFlow<Resource<R>>,
    loadingView: LoadingView? = null,
    error: ((Resource<R>) -> Unit)? = null,
    singleEvent: Boolean = false,
    @UiThread
    process: (R) -> Unit,
) {
    viewLifecycleOwner.collect(stateFlow) {
        it.apply {
            if (handled) {
                return@apply
            }
            when (this.status) {
                Status.SUCCESS -> {
                    Alerter.clearCurrent(requireActivity())
                    if (loadingView == null)
                        showProgressAlert()
                    data?.let { it1 -> process.invoke(it1) }
                    loadingView?.onStopLoading(true)
                    handled = singleEvent

                }
                Status.ERROR -> {
                    Alerter.clearCurrent(requireActivity())
                    if (error == null) {
                        throwable?.let { exception ->
                            onError(exception)
                        }
                        message?.let {
                            showAlert(it)
                        }
                    } else {
                        error.invoke(it)
                    }
                    loadingView?.onStopLoading(
                        false,
                        throwable?.localizedMessage ?: it.message.orEmpty()
                    )
                    handled = singleEvent

                }
                Status.LOADING -> {
                    loadingView?.onInit()
                    loadingView?.onStartLoading()
                    if (loadingView == null)
                        showProgressAlert()
                }
            }
        }
    }
}

fun Fragment.onError(exception: Throwable) {
    showAlert(
        exception.localizedMessage ?: exception.message
        ?: getString(R.string.something_went_wrong_please_try_again)
    );
}

fun ImageView.load(url: String, callback: ((Bitmap?) -> Unit)? = null) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .placeholder(R.drawable.ic_outline_image_24)
        .error(R.drawable.ic_outline_image_24)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                callback?.invoke(null)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                callback?.invoke(resource)
                return false
            }
        })

        .into(this)
}

fun <T : Any, L : StateFlow<T>> LifecycleOwner.collect(stateFlow: L, body: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            stateFlow.collectLatest {
                body.invoke(it)
            }
        }
    }
}

inline fun <reified T : BaseState<T>, R> ViewModel.launchInBackGround(
    stateFlow: MutableStateFlow<T>,
    crossinline process: () -> Flow<Resource<R>>,
    crossinline map: (R) -> T
) {
    viewModelScope.launch(Dispatchers.IO) {
        process.invoke()
            .collectLatest {
                when (it.status) {
                    Status.ERROR -> {
                        stateFlow.value.isLoading = false
                        stateFlow.value.toJson().fromJson<T>()?.apply {
                            this.isLoading = false
                            this.error = it.message
                            this.throwable = it.throwable
                            stateFlow.value = this
                        }

                    }
                    Status.LOADING -> {
                        stateFlow.value.apply {
                            this.isLoading = true
                        }
                    }
                    Status.SUCCESS -> {
                        stateFlow.value.isLoading = false
                        it.data?.let { data ->
                            stateFlow.value = map(data)
                        }
                    }
                }


            }

    }

}
