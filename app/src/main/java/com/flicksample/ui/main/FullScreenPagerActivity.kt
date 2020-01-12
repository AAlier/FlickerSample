package com.flicksample.ui.main

import android.view.View
import com.alexvasilkov.gestures.commons.DepthPageTransformer
import com.alexvasilkov.gestures.transition.GestureTransitions
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker
import com.flicksample.R
import com.flicksample.model.Photo
import com.flicksample.ui.base.BaseActivity
import com.flicksample.ui.main.adapter.PagerViewHolder
import com.flicksample.ui.main.adapter.PhotoPagerAdapter
import com.flicksample.ui.main.adapter.PhotoViewHolder
import com.flicksample.util.DecorUtils
import kotlinx.android.synthetic.main.view_list_photos.*
import kotlinx.android.synthetic.main.view_pager_photos.*

abstract class FullScreenPagerActivity : BaseActivity() {
    private lateinit var pagerAdapter: PhotoPagerAdapter
    private lateinit var listAnimator: ViewsTransitionAnimator<Int>

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initPager()
        initPagerAnimator()
        initDecorMargins()
    }

    private fun initPager() {
        pagerAdapter = PhotoPagerAdapter(viewPager)

        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(true, DepthPageTransformer())

        pagerToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        pagerToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initPagerAnimator() {
        val gridTracker = object : SimpleTracker() {
            public override fun getViewAt(pos: Int): View? {
                val holder = recyclerView.findViewHolderForLayoutPosition(pos)
                return if (holder != null && holder is PhotoViewHolder) {
                    holder.itemView.findViewById(R.id.imageView)
                } else null
            }
        }

        val pagerTracker = object : SimpleTracker() {
            public override fun getViewAt(pos: Int): View? {
                val holder = pagerAdapter.getViewHolder(pos)
                return if (holder != null && holder is PagerViewHolder) {
                    holder.itemView.findViewById(R.id.imageView)
                } else null
            }
        }

        listAnimator = GestureTransitions.from<Int>(recyclerView, gridTracker)
            .into(viewPager, pagerTracker)

        listAnimator.addPositionUpdateListener { position, isLeaving ->
            this.applyFullPagerState(
                position,
                isLeaving
            )
        }
    }

    private fun isSystemUiShown(): Boolean {
        return window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0
    }

    private fun applyFullPagerState(position: Float, isLeaving: Boolean) {
        background.visibility = if (position == 0f) View.INVISIBLE else View.VISIBLE
        background.alpha = position

        pagerToolbar.visibility = if (position == 0f) View.INVISIBLE else View.VISIBLE
        pagerToolbar.alpha = if (isSystemUiShown()) position else 0f
    }

    override fun onBackPressed() {
        if (!listAnimator.isLeaving) {
            listAnimator.exit(true)
        } else {
            super.onBackPressed()
        }
    }

    protected fun onExpandImage(position: Int) {
        pagerAdapter.setActivated(true);
        listAnimator.enter(position, true);
    }

    protected fun setPhotos(list: List<Photo>) {
        pagerAdapter.setPhotos(list)
    }

    private fun initDecorMargins() {
        DecorUtils.paddingForStatusBar(toolbar, true)
        DecorUtils.paddingForStatusBar(pagerToolbar, true)
        DecorUtils.paddingForNavBar(recyclerView)
    }
}