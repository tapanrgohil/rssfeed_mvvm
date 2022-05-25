package com.tapan.avomatest.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

class GlideImageGetter(private val context: Context, private val textView: TextView) :
    Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val drawable = BitmapDrawablePlaceHolder(textView)
        Glide.with(context)
            .load(source)
            .into(drawable)
        return drawable
    }

    class BitmapDrawablePlaceHolder() : BitmapDrawable(),
        com.bumptech.glide.request.target.Target<Drawable> {

        private lateinit var textView: TextView

        constructor(textView: TextView) : this() {
            this.textView = textView
        }

        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            drawable?.draw(canvas)

        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            textView.text = textView.text
        }

        override fun onResourceReady(
            resource: Drawable,
            transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
        ) {
            setDrawable(resource)
        }

        /**
         * Imitate SimpleTarget
         */

        private var request: Request? = null

        override fun onLoadStarted(placeholder: Drawable?) {
            //Do nothing
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            //Do nothing
        }

        override fun getSize(cb: SizeReadyCallback) {
            cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL)
        }

        override fun getRequest(): Request? = request

        override fun onStop() {
            //Do nothing
        }

        override fun setRequest(request: Request?) {
            this.request = request
        }

        override fun removeCallback(cb: SizeReadyCallback) {
            //Do nothing
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            //Do nothing
        }

        override fun onStart() {
            //Do nothing
        }

        override fun onDestroy() {
            //Do nothing
        }
    }

}