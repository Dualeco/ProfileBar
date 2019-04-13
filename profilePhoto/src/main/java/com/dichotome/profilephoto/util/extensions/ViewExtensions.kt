package com.dichotome.profilephoto.util.extensions

import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.views.RoundedImageView
import kotlin.math.ceil


fun View.getZoomWidth() = Constants(context).DISPLAY_WIDTH
fun View.getZoomHeight() = Constants(context).DISPLAY_HEIGHT


fun View.getViewCenteredX() = ceil((getZoomWidth() - layoutParams.width) / 2f)
fun View.getViewCenteredY() = ceil((getZoomHeight() - layoutParams.height) / 2f)

fun View.setInCenter() {
    x = getViewCenteredX()
    y = getViewCenteredY()
}

fun View.setOnBackButtonClick(condition: () -> Boolean, onClick: () -> Unit) {
    isFocusableInTouchMode = true
    requestFocus()
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && condition()) {
            onClick()
            true
        } else false
    }
}

fun RoundedImageView.copyForOverlay(imageView: RoundedImageView) = apply {
    cornerRadius = imageView.cornerRadius
    layoutParams = android.widget.FrameLayout.LayoutParams(
        imageView.measuredWidth,
        imageView.measuredHeight
    )
    val coords = IntArray(2)
    imageView.getLocationInWindow(coords)

    x = coords[0].toFloat()
    y = coords[1].toFloat()

    setImageDrawable(imageView.drawable)
}