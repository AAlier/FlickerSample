package com.flicksample.ui.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flicksample.model.Photo
import com.flicksample.ui.base.DefaultEndlessRecyclerAdapter

class PhotoListAdapter(private val listener: PhotoListener) :
    DefaultEndlessRecyclerAdapter<PhotoViewHolder>() {
    private var photos: List<Photo>? = null
    var hasMore = true
        private set

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return if(photos != null && photos!!.size > position) photos!!.get(position).id.hashCode().toLong() else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EXTRA_ITEM_TYPE -> PhotoViewHolder(parent, listener)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoViewHolder)
            holder.bind(photos!![position])
    }

    override fun getCount(): Int {
        return if (photos != null) photos!!.size else 0
    }

    fun setPhotos(photos: List<Photo>, hasMore: Boolean) {
        val old = this.photos
        this.photos = photos
        this.hasMore = hasMore
        notifyDataSetChanged()
        // PhotoDiffUtil.notifyChanges(this, old, photos)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is PhotoViewHolder)
            holder.unbind()
    }

    fun clearAll() {
        this.photos = ArrayList()
        notifyDataSetChanged()
    }

}