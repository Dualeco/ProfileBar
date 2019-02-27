package com.dichotome.collapsingbar.util.view.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.dichotome.collapsingbar.os.ProfileBarApp
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


fun isCameraAvailable(): Boolean {
    val app = ProfileBarApp.app
    return app.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showKeyboard(view: View? = currentFocus) {
    view?.let {
        it.requestFocus()
        it.performClick()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}