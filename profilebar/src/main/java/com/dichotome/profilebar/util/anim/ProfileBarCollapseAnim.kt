package com.dichotome.profilebar.util.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.dichotome.profilebar.ui.profileBar.toolbar.ProfileAnimatedToolbar.Companion.TAG
import com.dichotome.profilephoto.ui.ZoomingImageView
import com.dichotome.profileshared.anim.LinearAnimationHelper
import com.dichotome.profileshared.anim.PlainAnimationHelper

class HorizontalAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {

    private var initX: Int = view.left
    override fun evaluate(): ObjectAnimator? {

        val delta = initX - view.left + view.translationX
        val anim = animateFloat("translationX", delta, 0f)
        initX = view.left

        return anim
    }
}

class ReturnAlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {
    override fun evaluate(): ObjectAnimator? {

        return if (view.alpha in 0.2..0.4 || view.alpha == 1f)
            animateFloat("alpha", 1f, 0.3f, returnTo = 1f)
        else {
            animateFloat("alpha", view.alpha, 1f, AccelerateInterpolator())
        }
    }
}

class AlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long,
    private val startValue: Float = 1f,
    private val endValue: Float
) : LinearAnimationHelper(target, interpolator, duration) {
    private var collapsed = true
    override fun evaluate() = animateFloat(
        "alpha",
        if (collapsed) startValue else endValue,
        if (collapsed) endValue else startValue
    ).also {
        collapsed = !collapsed
    }
}

class SmoothAlphaAnimationHelper(
    target: View,
    duration: Long
) : LinearAnimationHelper(target, null, duration) {
    override fun evaluate(): ObjectAnimator? {
        val visible = (view.alpha == 1f && view.isVisible)
        return animateFloat(
            "alpha",
            if (visible) 1f else 0f,
            if (visible) 0f else 1f,
            if (visible) DecelerateInterpolator() else AccelerateInterpolator()
        )
    }
}

class RotationAnimationHelper(
    target: View,
    interpolator: TimeInterpolator = LinearInterpolator(),
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {
    private var collapsed = true
    override fun evaluate(): ObjectAnimator? {
        val anim = animateInt(
            "rotation",
            if (collapsed) 180 else 90,
            if (collapsed) 90 else 180
        )
        collapsed = !collapsed

        return anim
    }
}

class PlainTranslationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {

    private var initX = view.left
    private var initY = view.bottom

    override fun evaluateXY(): Pair<Animator?, Animator?> {

        val deltaX = initX.toFloat() - view.left + view.translationX
        val animX = animateFloat("translationX", deltaX, 0f)
        initX = view.left

        val deltaY = initY.toFloat() - view.bottom + view.translationY
        val animY = animateFloat("translationY", deltaY, 0f)
        initY = view.bottom

        return Pair(animX, animY)
    }
}

class ReturnScaleAnimationHelper(
    private val scaleTo: Float = 0.4f,
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {
    override fun evaluateXY(): Pair<Animator?, Animator?> {

        val animX: ObjectAnimator?
        val animY: ObjectAnimator?

        if (view.scaleX in (scaleTo - 0.1)..(scaleTo + 0.1) || view.scaleX == 1f) {
            animX = animateFloat("scaleX", view.scaleX, scaleTo, returnTo = 1f)
            animY = animateFloat("scaleY", view.scaleY, scaleTo, returnTo = 1f)
        } else {
            animX = animateFloat("scaleX", view.scaleX, 1f, AccelerateInterpolator(), duration * view.scaleX.toLong())
            animY = animateFloat("scaleY", view.scaleY, 1f, AccelerateInterpolator(), duration * view.scaleY.toLong())
        }

        return Pair(animX, animY)
    }
}