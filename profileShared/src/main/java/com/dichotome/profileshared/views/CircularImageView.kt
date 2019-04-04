package com.dichotome.profileshared.views

import android.content.Context
import android.util.AttributeSet

open class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RoundedImageView(context, attrs, defStyle) {
    init {
        isCircular = true
    }
}