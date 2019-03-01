package com.dichotome.profilebar.util.anim

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.util.Pair
import com.dichotome.profilebar.util.view.profileBar.ProfileCollapsingToolbar.Companion.TAG

class HorizontalAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {

    private var initX: Int = view.left
    override fun evaluate(): ObjectAnimator? {

        val delta = initX.toFloat() - view.left + view.translationX
        val anim = animate("translationX", delta, 0f)
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
            animate("alpha", 1f, 0.3f, returnTo = 1f)
        else {
            animate("alpha", view.alpha, 1f, AccelerateInterpolator())
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
    override fun evaluate() = animate(
        "alpha",
        if (collapsed) startValue else endValue,
        if (collapsed) endValue else startValue
    ).also {
        collapsed = !collapsed
    }
}

class RotationAnimationHelper(
    target: View,
    interpolator: TimeInterpolator = LinearInterpolator(),
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {
    private var collapsed = true
    override fun evaluate(): ObjectAnimator? {
        val anim = animate(
            "rotation",
            if (collapsed) 180f else 90f,
            if (collapsed) 90f else 180f
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

    override fun evaluateXY(): Pair<ObjectAnimator?, ObjectAnimator?> {

        val deltaX = initX.toFloat() - view.left + view.translationX
        val animX = animate("translationX", deltaX, 0f)
        initX = view.left

        Log.d(TAG, "$initY ${view.bottom}")

        val deltaY = initY.toFloat() - view.bottom + view.translationY
        val animY = animate("translationY", deltaY, 0f)
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
    override fun evaluateXY(): Pair<ObjectAnimator?, ObjectAnimator?> {

        val animX: ObjectAnimator?
        val animY: ObjectAnimator?

        if (view.scaleX in (scaleTo - 0.1)..(scaleTo + 0.1) || view.scaleX == 1f) {
            animX = animate("scaleX", view.scaleX, scaleTo, returnTo = 1f)
            animY = animate("scaleY", view.scaleY, scaleTo, returnTo = 1f)
        } else {
            animX = animate("scaleX", view.scaleX, 1f, AccelerateInterpolator(), duration * view.scaleX.toLong())
            animY = animate("scaleY", view.scaleY, 1f, AccelerateInterpolator(), duration * view.scaleY.toLong())
        }


        return Pair(animX, animY)
    }
}