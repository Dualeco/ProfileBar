package com.dichotome.profilebar.util.view.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.util.constant.dpToPx


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
        dpToPx(this,56)
    }
}

fun Context.getNavBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun ImageView.download(url: String, isCircular: Boolean) {
    val options = RequestOptions()
        .error(R.drawable.ic_broken_image)

    if (isCircular) {
        options.circleCrop()
    }

    Glide.with(context)
        .load(url)
        .apply(options)
        .into(this)
}

fun ImageView.download(drw: Drawable?, isCircular: Boolean) {
    val options = RequestOptions()
        .error(R.drawable.ic_broken_image)

    if (isCircular) {
        options.circleCrop()
    }

    Glide.with(context)
        .load(drw)
        .apply(options)
        .into(this)
}

fun <T> T.addTo(collection: MutableCollection<T>): T {
    collection.add(this)
    return this
}

fun ConstraintLayout.addChildren(collection: Collection<View>) {
    collection.forEach {
        addView(it)
    }
}
