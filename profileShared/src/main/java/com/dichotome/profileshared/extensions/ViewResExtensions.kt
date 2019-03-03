package com.dichotome.profileshared.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.dichotome.profileshared.constants.*

fun View.str(@StringRes res: Int): String = ResourceUtil(context).str(res)

fun View.str(@StringRes res: Int, vararg args: String): String = ResourceUtil(context).str(res, *args)

fun View.col(@ColorRes res: Int): Int = ResourceUtil(context).col(res)

fun View.drw(@DrawableRes res: Int): Drawable = ResourceUtil(context).drw(res)

fun View.dpToPx(dp: Int): Int = ResourceUtil(context).dpToPx(dp)

fun View.dpToPx(dp: Float): Int = ResourceUtil(context).dpToPx(dp)

fun View.pxToDp(px: Float): Int = ResourceUtil(context).pxToDp(px)

fun View.pxToDp(px: Int): Int =  ResourceUtil(context).pxToDp(px)