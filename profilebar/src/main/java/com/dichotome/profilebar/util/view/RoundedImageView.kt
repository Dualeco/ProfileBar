package com.dichotome.profilebar.util.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import com.dichotome.profilebar.R

class RoundedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true

            val cRadius = getCornerRadius(context, attrs)
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = cRadius
            }
        }
    }

    private fun getCornerRadius(context: Context, attrs: AttributeSet?): Float {
        var cornerRadius = 0.0f
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
}