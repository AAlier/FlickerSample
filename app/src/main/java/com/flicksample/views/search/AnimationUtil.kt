package com.flicksample.views.search

import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils


object AnimationUtils {

    private const val ANIMATION_DURATION_SHORT = 250

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    fun circleRevealView(view: View, duration: Int = ANIMATION_DURATION_SHORT) {
        val cx = view.width
        val cy = view.height / 2

        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

        if (duration > 0) {
            anim.duration = duration.toLong()
        } else {
            anim.duration = ANIMATION_DURATION_SHORT.toLong()
        }

        view.visibility = View.VISIBLE
        anim.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    fun circleHideView(
        view: View,
        listenerAdapter: AnimatorListenerAdapter,
        duration: Int = ANIMATION_DURATION_SHORT
    ) {
        val cx = view.width
        val cy = view.height / 2

        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)

        anim.addListener(listenerAdapter)

        if (duration > 0) {
            anim.duration = duration.toLong()
        } else {
            anim.duration = ANIMATION_DURATION_SHORT.toLong()
        }

        anim.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmOverloads
    fun circleRevealViewFromBottom(view: View, duration: Int = ANIMATION_DURATION_SHORT) {
        val cx = view.width / 2
        val cy = view.height / 2

        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

        if (duration > 0) {
            anim.duration = duration.toLong()
        } else {
            anim.duration = ANIMATION_DURATION_SHORT.toLong()
        }

        view.visibility = View.VISIBLE
        anim.start()
    }
}