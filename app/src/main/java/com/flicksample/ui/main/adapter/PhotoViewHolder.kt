package com.flicksample.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flicksample.R
import com.flicksample.model.Photo
import com.flicksample.util.GlideHelper
import com.flicksample.util.inflater
import com.flicksample.util.setOnDelayedClickListener
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoViewHolder(
    parent: ViewGroup,
    private val listener: PhotoListener
) : RecyclerView.ViewHolder(inflater(parent, R.layout.item_photo, false)) {

    fun bind(item: Photo) {
        itemView.apply {
            GlideHelper.loadFlickrThumb(item, imageView)
            setOnDelayedClickListener {
                listener.onClickPhoto(adapterPosition)
            }
        }
    }

    fun unbind() {
        itemView.apply {
            GlideHelper.clear(imageView)
            imageView.setImageDrawable(null)
        }
    }
}