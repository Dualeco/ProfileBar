package com.dichotome.collapsingbar.util.view.extensions

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable


fun Drawable.setColor(color: Int): Drawable {
    var canBeColored = false
    var drawable: GradientDrawable? = null
    try {
        drawable = this as GradientDrawable
    } catch (ex: Exception) {
        canBeColored = false
    }
    if (canBeColored)
        drawable?.setColor(color)
    return this
}