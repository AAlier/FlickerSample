package com.flicksample.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.flicksample.R
import com.flicksample.model.Photo
import com.flicksample.util.GlideHelper
import com.flicksample.util.inflater
import kotlinx.android.synthetic.main.item_full_screen.view.*

private const val PROGRESS_DELAY = 300L

class PagerViewHolder(
    parent: ViewGroup,
    viewPager: ViewPager
) :
    RecyclePagerAdapter.ViewHolder(inflater(parent, R.layout.item_full_screen, false)) {

    init {
        itemView.imageView.controller.enableScrollInViewPager(viewPager)
        itemView.imageView.positionAnimator.addPositionUpdateListener { position, _ ->
            itemView.progressBar.visibility =
                if (position == 1f) View.VISIBLE else View.INVISIBLE
        }
    }

    fun bind(photo: Photo) {
        itemView.progressBar.animate().setStartDelay(PROGRESS_DELAY).alpha(1f)
        GlideHelper.loadFlickrFull(photo, itemView.imageView, progressListener)
    }

    private val progressListener = object : GlideHelper.LoadingListener {
        override fun onSuccess() {
            itemView.progressBar.animate().cancel()
            itemView.progressBar.animate().alpha(0f)
        }

        override fun onError() {
            itemView.progressBar.animate().alpha(0f)
        }
    }

    fun unbind() {
        GlideHelper.clear(itemView.imageView)
        itemView.progressBar.animate().cancel()
        itemView.progressBar.alpha = 0f
        itemView.imageView.setImageDrawable(null)
    }
}