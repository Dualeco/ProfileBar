package com.dichotome.profileshared.extensions

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.dichotome.profileshared.constants.ResourceUtil


fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        Math.ceil((25 * resources.displayMetrics.density).toDouble()).toInt()
    }
}

fun Context.getToolbarHeight(): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else {
        ResourceUtil(this).dpToPx(56)
    }
}

fun Context.getNavBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun <T> T.addTo(collection: MutableCollection<T>): T {
    collection.add(this)
    return this
}

fun View.setViewAndChildrenEnabled(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.setViewAndChildrenEnabled(enabled)
        }
    }
}

fun <T : View> ViewGroup.addAndGetView(view: T) = view.also { addView(it) }