package com.dichotome.profilebarapp.util.constant

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.dichotome.profilebarapp.os.ProfileBarApp


fun str(@StringRes res: Int): String = ProfileBarApp.app.getString(res)

fun str(@StringRes res: Int, vararg args: String): String = ProfileBarApp.app.getString(res, *args)

fun col(@ColorRes res: Int): Int = ContextCompat.getColor(ProfileBarApp.app, res)

fun drw(@DrawableRes res: Int): Drawable = ContextCompat.getDrawable(ProfileBarApp.app, res) ?: ShapeDrawable()

fun dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

fun dpToPx(dp: Float): Int = Math.round(dp * ProfileBarApp.app.resources.displayMetrics.density)