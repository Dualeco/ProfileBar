package com.dichotome.profilebar.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class SquareImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(width, width)
    }
}