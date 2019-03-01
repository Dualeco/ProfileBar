package com.dichotome.profilebar.util.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import com.dichotome.profilebar.R

class SquareRoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    val makeRound: Boolean = false
) :
    ImageView(context, attrs, defStyle) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true

            val cRadius = extractCornerRadius(context, attrs)
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = cRadius
            }
        }
    }

    var cornerRadius = 0.0f
        set(value) {
            field = value
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = field
            }
        }

    private fun extractCornerRadius(context: Context, attrs: AttributeSet?): Float {
        cornerRadius = 0.0f
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedImageView,
            0, 0
        ).apply {
            try {
                cornerRadius = getDimension(R.styleable.RoundedImageView_cornerRadius, 0.0f)
            } finally {
                recycle()
            }
        }

        return cornerRadius
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth

        if (makeRound)
            cornerRadius = measuredWidth / 2f

        setMeasuredDimension(width, width)
    }
}