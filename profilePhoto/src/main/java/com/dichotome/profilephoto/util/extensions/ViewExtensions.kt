package com.dichotome.profilephoto.util.extensions

import android.view.KeyEvent
import android.view.View
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.views.CircularImageView
import kotlin.math.ceil


fun View.getZoomWidth() = Constants(context).DISPLAY_WIDTH
fun View.getZoomHeight() = Constants(context).DISPLAY_HEIGHT


fun View.getViewCenteredX() = ceil((getZoomWidth() - layoutParams.width) / 2f)
fun View.getViewCenteredY() = ceil((getZoomHeight() - layoutParams.height) / 2f)

fun View.setInCenter() {
    x = getViewCenteredX()
    y = getViewCenteredY()
}

fun View.setOnBackButtonClicked(condition: () -> Boolean, onClicked: () -> Unit) {
    isFocusableInTouchMode = true
    requestFocus()
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && condition()) {
            onClicked()
            true
        } else false
    }
}

fun CircularImageView.copyForOverlay(imageView: CircularImageView) = apply {
    cornerRadius = imageView.cornerRadius
    adjustViewBounds = true
    layoutParams = android.widget.FrameLayout.LayoutParams(imageView.measuredWidth, imageView.measuredWidth)

    val coords = android.graphics.Rect()
    imageView.getGlobalVisibleRect(coords)

    x = coords.left.toFloat()
    y = coords.top.toFloat() - Constants(context).STATUS_BAR_SIZE

    setImageDrawable(imageView.drawable)
}