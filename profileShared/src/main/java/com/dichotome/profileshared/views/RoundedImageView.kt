package com.dichotome.profileshared.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.dichotome.profileshared.R
import kotlin.math.min

open class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ImageView(context, attrs, defStyle) {

    init {
        clipToOutline = true

        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = extractCornerRadius(context, attrs)
        }
    }

    var isSquare = false
    var isCircular = false
        set(value) {
            field = value
            if (value) isSquare = value
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val dimension = min(measuredWidth, measuredHeight)
        when {
            isCircular -> cornerRadius = dimension / 2
            isSquare -> setMeasuredDimension(dimension, dimension)
        }
    }

    var cornerRadius: Int = extractCornerRadius(context, attrs).toInt()
        set(value) {
            field = value
            (background as? GradientDrawable)?.let {
                it.cornerRadius = field.toFloat()
            }
        }

    private fun extractCornerRadius(context: Context, attrs: AttributeSet?): Float {
        var radius = 0f
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedImageView,
            0, 0
        ).apply {
            try {
                radius = getDimension(R.styleable.RoundedImageView_cornerRadius, 0.0f)
            } finally {
                recycle()
            }
        }
        return radius
    }
}