package com.flicksample.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

import androidx.appcompat.widget.AppCompatImageView
import com.flicksample.R

class AspectImageView : AppCompatImageView {

    private var aspect = DEFAULT_ASPECT

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val arr = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.aspect))
        aspect = arr.getFloat(0, aspect)
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.AT_MOST) {
            height = calculate(width, aspect,
                VERTICAL
            )
        } else if (heightMode == View.MeasureSpec.EXACTLY || heightMode == View.MeasureSpec.AT_MOST) {
            width = calculate(height, aspect,
                HORIZONTAL
            )
        } else if (width != 0) {
            height = calculate(width, aspect,
                VERTICAL
            )
        } else if (height != 0) {
            width = calculate(height, aspect,
                HORIZONTAL
            )
        } else {
            Log.e(
                AspectImageView::class.java.simpleName,
                "Either width or height should have exact value"
            )
        }

        val specWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val specHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        super.onMeasure(specWidth, specHeight)
    }

    private fun calculate(size: Int, aspect: Float, direction: Int): Int {
        val wp = paddingLeft + paddingRight
        val hp = paddingTop + paddingBottom
        return if (direction == VERTICAL)
            Math.round((size - wp) / aspect) + hp
        else
            Math.round((size - hp) * aspect) + wp
    }

    companion object {

        val DEFAULT_ASPECT = 16f / 9f

        private val VERTICAL = 0
        private val HORIZONTAL = 0
    }

}
