package com.dichotome.profilephoto.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.dichotome.profileviewsshared.anim.LinearAnimationHelper
import com.dichotome.profileviewsshared.anim.PlainAnimationHelper
import com.dichotome.profileviewsshared.anim.SimpleAnimationHelper
import com.dichotome.profileviewsshared.constants.Constants
import com.dichotome.profileviewsshared.views.SquareRoundedImageView


class DetachFromFrameAnimationHelper(
    private val targetView: SquareRoundedImageView,
    private val detachFrom: SquareRoundedImageView,
    private val TimeInterpolator: TimeInterpolator,
    private val animDuration: Long
) : LinearAnimationHelper(targetView, TimeInterpolator, animDuration) {

    private val init = targetView.cornerRadius
    private var rounding = false
    override fun evaluate(): ObjectAnimator? {

        val endValue = if (rounding) init else 0
        rounding = !rounding

        return animateInt("cornerRadius", targetView.cornerRadius, endValue)?.apply {
            doOnStart {
                if (rounding) {
                    detachFrom.isVisible = false
                    view.isVisible = true
                }
            }
            doOnEnd {
                if (!rounding && targetView.cornerRadius == init) {
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
    private var zoomIn = true
    override fun evaluateXY(): Pair<Animator?, Animator?> {

        val animX: ObjectAnimator?
        val animY: ObjectAnimator?

        val ratio = Constants(view.context).DISPLAY_WIDTH.toFloat() / view.layoutParams.width
        val endValue = if (zoomIn) ratio else 1f

        animX = animateFloat("scaleX", view.scaleX, endValue)
        animY = animateFloat("scaleY", view.scaleY, endValue)

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

    private val height = Constants(view.context).DISPLAY_HEIGHT - Constants(view.context).NAVBAR_SIZE
    private val width = Constants(view.context).DISPLAY_WIDTH
    private val dimension = view.layoutParams.width
    private var zoomIn = true
    override fun evaluateXY(): Pair<Animator?, Animator?> {
        val direction = if (zoomIn) 1 else -1

        val viewCenterEndX = if (zoomIn) width / 2f else view.x  + dimension / 2f
        val viewCenterEndY = if (zoomIn) height / 2f else view.y + dimension / 2f

        val offsetX = viewCenterEndX - initX - dimension / 2
        val offsetY = viewCenterEndY - initY - dimension / 2

        val animX = animateFloat("translationX", view.x, view.x + offsetX * direction)
        val animY = animateFloat("translationY", view.y, view.y + offsetY * direction)

        zoomIn = !zoomIn

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

    private var zoomIn = true

    private val centerX = (photoView.x + photoView.layoutParams.width.toFloat() / 2).toInt()
    private val centerY = (photoView.y + photoView.layoutParams.height.toFloat() / 2).toInt()

    override fun evaluate(): Animator? {
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
                if (!zoomIn) viewToReveal.isVisible = true
            }
            doOnEnd {
                if (zoomIn) viewToReveal.isVisible = false
            }

            zoomIn = !zoomIn
            interpolator = TimeInterpolator
            duration = animDuration
            start()
        }
    }
}