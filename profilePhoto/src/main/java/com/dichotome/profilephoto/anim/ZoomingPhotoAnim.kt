package com.dichotome.profilephoto.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.dichotome.profileshared.anim.LinearAnimationHelper
import com.dichotome.profileshared.anim.PlainAnimationHelper
import com.dichotome.profileshared.anim.SimpleAnimationHelper
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.views.SquareRoundedImageView
import kotlin.math.ceil

fun getZoomWidth(context: Context) = Constants(context).DISPLAY_WIDTH
fun getZoomHeight(context: Context) = Constants(context).DISPLAY_HEIGHT
private fun getStatusBarHeight(context: Context) = Constants(context).STATUS_BAR_SIZE


fun getViewCenteredX(view: View) = ceil((getZoomWidth(view.context) - view.layoutParams.width) / 2f)
fun getViewCenteredY(view: View) = ceil((getZoomHeight(view.context) - view.layoutParams.height) / 2f)

class DetachFromFrameAnimationHelper(
    private val targetView: SquareRoundedImageView,
    private val detachFrom: SquareRoundedImageView,
    timeInterpolator: TimeInterpolator,
    animDuration: Long
) : LinearAnimationHelper(targetView, timeInterpolator, animDuration) {

    private val init = targetView.cornerRadius
    override fun evaluate(): ObjectAnimator? {
        val rounding = view.isVisible

        val endValue = if (rounding) init else 0

        return animateInt("cornerRadius", targetView.cornerRadius, endValue)?.apply {
            doOnStart {
                if (!rounding) {
                    detachFrom.isVisible = false
                    view.isVisible = true
                }
            }
            doOnEnd {
                if (rounding && targetView.cornerRadius == init) {
                    view.isVisible = false
                    detachFrom.isVisible = true
                }
            }
            start()
        }
    }
}

class ZoomAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {
    override fun evaluateXY(): Pair<Animator?, Animator?> {
        val zoomIn = view.scaleX == 1f

        val zoomHeight = getZoomHeight(view.context)
        val zoomWidth = getZoomWidth(view.context)

        val ratio = (min(zoomWidth, zoomHeight).toFloat()) / view.layoutParams.width
        val roundedRatio = ceil(ratio * 10) / 10

        val endValue = if (zoomIn) roundedRatio else 1f

        val animX = animateFloat("scaleX", view.scaleX, endValue)
        val animY = animateFloat("scaleY", view.scaleY, endValue)

        return Pair(animX, animY)
    }

    private fun min(a: Int, b: Int) = if (a < b) a else b
}

class ZoomTranslationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {

    private var initX = view.x
    private var initY = view.y

    private val height = getZoomHeight(view.context)
    private val width = getZoomWidth(view.context)

    private val dimension = view.layoutParams.width

    override fun evaluateXY(): Pair<Animator?, Animator?> {
        val zoomIn = view.scaleX == 1f
        val direction = if (zoomIn) 1 else -1

        val viewCenterEndX = if (zoomIn) width / 2f else view.x  + dimension / 2f
        val viewCenterEndY = if (zoomIn) height / 2f else view.y + dimension / 2f

        val offsetX = viewCenterEndX - initX - dimension / 2
        val offsetY = viewCenterEndY - initY - dimension / 2

        val animX = animateFloat("translationX", view.x, view.x + offsetX * direction)
        val animY = animateFloat("translationY", view.y, view.y + offsetY * direction)

        return Pair(animX, animY)
    }
}

class ZoomCircularRevealHelper(
    photoView: SquareRoundedImageView,
    private val viewToReveal: View,
    private val TimeInterpolator: TimeInterpolator,
    private var startRadius: Int,
    private var stopRadius: Int,
    private val animDuration: Long
) : SimpleAnimationHelper(photoView, TimeInterpolator, animDuration) {

    private val centerX = (photoView.x + photoView.layoutParams.width.toFloat() / 2).toInt()
    private val centerY = (photoView.y + photoView.layoutParams.height.toFloat() / 2).toInt()

    override fun evaluate(): Animator? {

        val zoomIn = !viewToReveal.isVisible

        val beginRadius = if (zoomIn) startRadius else stopRadius
        val endRadius = if (zoomIn) stopRadius else startRadius

        return ViewAnimationUtils.createCircularReveal(
            viewToReveal,
            centerX,
            centerY,
            beginRadius.toFloat(),
            endRadius.toFloat()
        ).apply {
            doOnStart {
                if (zoomIn) viewToReveal.isVisible = true
            }
            doOnEnd {
                if (!zoomIn) viewToReveal.isVisible = false
            }

            interpolator = TimeInterpolator
            duration = animDuration
            start()
        }
    }
}