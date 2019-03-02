package com.dichotome.profileviewsshared.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.dichotome.profileviewsshared.R

open class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    private val round: Boolean = false
) :
    ImageView(context, attrs, defStyle) {

    init {
        clipToOutline = true

        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = extractCornerRadius(context, attrs)
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