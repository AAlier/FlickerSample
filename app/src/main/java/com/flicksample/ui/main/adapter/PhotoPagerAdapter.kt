package com.flicksample.ui.main.adapter

import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter
import com.flicksample.model.Photo

class PhotoPagerAdapter(
    private val viewPager: ViewPager
) : RecyclePagerAdapter<PagerViewHolder>() {
    private var photos: List<Photo>? = null
    private var activated: Boolean = false

    fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    fun getPhoto(pos: Int): Photo? {
        return if (photos == null || pos < 0 || pos >= photos!!.size) null else photos!![pos]
    }

    /**
     * To prevent ViewPager from holding heavy views (with bitmaps)  while it is not showing
     * we may just pretend there are no items in this adapter ("activate" = false).
     * But once we need to run opening animation we should "activate" this adapter again.<br></br>
     * Adapter is not activated by default.
     */
    fun setActivated(activated: Boolean) {
        if (this.activated != activated) {
            this.activated = activated
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return if (!activated || photos == null) 0 else photos!!.size
    }

    override fun onCreateViewHolder(container: ViewGroup): PagerViewHolder {
        return PagerViewHolder(container, viewPager)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(photos!![position])
    }

    override fun onRecycleViewHolder(holder: PagerViewHolder) {
        super.onRecycleViewHolder(holder)
        holder.unbind()
    }
}
