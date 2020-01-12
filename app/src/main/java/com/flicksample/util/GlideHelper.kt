package com.flicksample.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import com.flicksample.model.Photo

object GlideHelper {
    private val TRANSITION =
        Transition<Drawable> { current, adapter ->
            if (adapter.view is ImageView) {
                val image = adapter.getView() as ImageView
                if (image.drawable == null) {
                    image.alpha = 0f
                    image.animate().alpha(1f)
                }
            }
            false
        }

    private val TRANSITION_FACTORY =
        TransitionFactory<Drawable> { dataSource, isFirstResource ->
            // Do not animate if image is loaded from memory
            if (dataSource == DataSource.REMOTE) TRANSITION else NoTransition.get()
        }

    fun loadFlickrThumb(photo: Photo, image: ImageView) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .dontTransform()

        val thumbRequest = Glide.with(image)
            .load(photo.thumbnailUrl)
            .apply(options)
            .transition(DrawableTransitionOptions.with(TRANSITION_FACTORY))

        Glide.with(image).load(photo.mediumUrl)
            .apply(options)
            .thumbnail(thumbRequest)
            .into(image)
    }

    fun loadFlickrFull(photo: Photo, image: ImageView, listener: LoadingListener) {
        val photoUrl = if (photo.largeUrl == null)
            photo.mediumUrl
        else
            photo.largeUrl

        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .dontTransform()

        val thumbRequest = Glide.with(image)
            .load(photo.thumbnailUrl)
            .apply(options)

        Glide.with(image)
            .load(photoUrl)
            .apply(RequestOptions().apply(options).placeholder(image.drawable))
            .thumbnail(thumbRequest)
            .listener(RequestListenerWrapper<Drawable>(listener))
            .into(image)
    }

    fun clear(view: ImageView) {
        Glide.with(view).clear(view)
        view.setImageDrawable(null)
    }

    interface LoadingListener {
        fun onSuccess()

        fun onError()
    }

    private class RequestListenerWrapper<T> internal constructor(private val listener: LoadingListener?) :
        RequestListener<T> {

        override fun onResourceReady(
            resource: T, model: Any, target: Target<T>,
            dataSource: DataSource, isFirstResource: Boolean
        ): Boolean {
            listener?.onSuccess()
            return false
        }

        override fun onLoadFailed(
            ex: GlideException?, model: Any,
            target: Target<T>, isFirstResource: Boolean
        ): Boolean {
            listener?.onError()
            return false
        }
    }

}