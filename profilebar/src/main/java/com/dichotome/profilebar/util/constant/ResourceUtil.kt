package com.dichotome.profilebar.util.constant

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


fun str(context: Context, @StringRes res: Int): String = context.getString(res)

fun str(context: Context, @StringRes res: Int, vararg args: String): String = context.getString(res, *args)

fun col(context: Context, @ColorRes res: Int): Int = ContextCompat.getColor(context, res)

fun drw(context: Context, @DrawableRes res: Int): Drawable = ContextCompat.getDrawable(context, res) ?: ShapeDrawable()

fun dpToPx(context: Context, dp: Int): Int = dpToPx(context, dp.toFloat())

fun dpToPx(context: Context, dp: Float): Int = Math.round(dp * context.resources.displayMetrics.density)