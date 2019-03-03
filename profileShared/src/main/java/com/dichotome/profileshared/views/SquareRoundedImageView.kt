package com.dichotome.profileshared.views

import android.content.Context
import android.util.AttributeSet

open class SquareRoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    RoundedImageView(context, attrs, defStyle) {

    private var dimension = 0
    private var circular = false

    open fun makeCircular() = also { circular = true }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        dimension = measuredWidth
        if (circular) cornerRadius = dimension / 2

        setMeasuredDimension(dimension, dimension)
    }
}