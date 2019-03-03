package com.dichotome.profileshared.constants

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ResourceUtil(val context: Context) {

    fun str(@StringRes res: Int): String = context.getString(res)

    fun str(@StringRes res: Int, vararg args: String): String = context.getString(res, *args)

    fun col(@ColorRes res: Int): Int = ContextCompat.getColor(context, res)

    fun drw(@DrawableRes res: Int): Drawable = ContextCompat.getDrawable(context, res) ?: ShapeDrawable()

    fun dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

    fun dpToPx(dp: Float): Int = Math.round(dp * context.resources.displayMetrics.density)

    fun pxToDp(px: Float): Int = Math.round(px / context.resources.displayMetrics.density)

    fun pxToDp(px: Int): Int = pxToDp(px.toFloat())
}