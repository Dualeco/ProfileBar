package com.dichotome.profilebar.util.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.dichotome.profilebar.util.constant.Constants
import com.dichotome.profilebar.util.view.SquareRoundedImageView
import kotlin.concurrent.timer


class DetachFromFrameAnimationHelper(
    private val targetView: SquareRoundedImageView,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(targetView, interpolator, duration) {

    private val init = targetView.cornerRadius
    private var rounding = false
    override fun evaluate(): ObjectAnimator? {

        val endValue = if (rounding) init else 0f
        rounding = !rounding

        return animate("cornerRadius", targetView.cornerRadius, endValue, autoStart = false)?.apply {
            doOnStart {
                if (rounding)
                    view.isVisible = true
            }
            doOnEnd {
                //(view.parent as ViewGroup).removeView(view)
                if (!rounding && targetView.cornerRadius == init)
                    view.isVisible = false
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
    private var zoomIn = true
    override fun evaluateXY(): Pair<ObjectAnimator?, ObjectAnimator?> {

        val animX: ObjectAnimator?
        val animY: ObjectAnimator?

        val ratio = Constants(view.context).DISPLAY_WIDTH.toFloat() / view.layoutParams.width
        val endValue = if (zoomIn) ratio else 1f

        animX = animate("scaleX", view.scaleX, endValue)
        animY = animate("scaleY", view.scaleY, endValue)

        zoomIn = !zoomIn


        return Pair(animX, animY)
    }
}

class ZoomTranslationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {

    private var initX = view.x
    private var initY = view.y

    private var zoomIn = true
    override fun evaluateXY(): Pair<ObjectAnimator?, ObjectAnimator?> {

        val height = Constants(view.context).DISPLAY_HEIGHT.toFloat() - Constants(view.context).NAVBAR_SIZE.toFloat()
        val width = Constants(view.context).DISPLAY_WIDTH.toFloat()
        val dimension = view.layoutParams.width.toFloat()


        val offsetX = width / 2f - initX - dimension / 2f
        val offsetY = height / 2f - initY - dimension / 2f

        val direction = if (zoomIn) 1 else -1

        val animX = animate("translationX", view.x, view.x + offsetX * direction)
        val animY = animate("translationY", view.y, view.y + offsetY * direction)

        zoomIn = !zoomIn

        return Pair(animX, animY)
    }
}

class ZoomCircularRevealHelper(
    photoView: SquareRoundedImageView,
    private val viewToReveal: View,
    private val timeInterpolator: TimeInterpolator,
    private val animDuration: Long
) : SimpleAnimationHelper(photoView, timeInterpolator, animDuration) {

    private var zoomIn = true

    private val centerX = (photoView.x + photoView.layoutParams.width.toFloat() / 2).toInt()
    private val centerY = (photoView.y + photoView.layoutParams.height.toFloat() / 2).toInt()

    private val revealRadius = photoView.cornerRadius
    private val completeRadius = Constants(view.context).DISPLAY_HEIGHT.toFloat()

    override fun evaluate(): Animator? {
        val startRadius = if (zoomIn) revealRadius else completeRadius
        val endRadius = if (zoomIn) completeRadius else revealRadius
        return ViewAnimationUtils.createCircularReveal(
            viewToReveal,
            centerX,
            centerY,
            startRadius,
            endRadius
        ).apply {
            doOnStart {
                if (!zoomIn) viewToReveal.isVisible = true
            }
            doOnEnd {
                if (zoomIn) viewToReveal.isVisible = false
            }

            zoomIn = !zoomIn
            interpolator = timeInterpolator
            duration = animDuration
            start()
        }
    }
}