package com.dichotome.profilebar.util.anim

import android.animation.Animator
import android.animation.TimeInterpolator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.util.Pair
import com.dichotome.profileshared.anim.LinearAnimationHelper
import com.dichotome.profileshared.anim.PlainAnimationHelper
import com.dichotome.profileshared.extensions.isDisplayed
import kotlin.math.max

class HorizontalAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {

    private var initX: Int = view.left

    override fun evaluate() = animateFloat(
        "translationX",
        initX - view.left + view.translationX,
        0f
    ).also {
        initX = view.left
    }
}

class ReturnAlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {
    override fun evaluate() = (view.alpha in 0.2..0.4 || view.alpha == 1f).let {
        if (it)
            animateFloat("alpha", 1f, 0.3f, returnTo = 1f)
        else
            animateFloat("alpha", view.alpha, 1f, AccelerateInterpolator())
    }
}

class AlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long,
    private val startValue: Float = 1f,
    private val endValue: Float
) : LinearAnimationHelper(target, interpolator, duration) {

    var transparent = view.alpha < max(startValue, endValue)

    override fun evaluate() = animateFloat(
        "alpha",
        if (transparent) startValue else endValue,
        if (transparent) endValue else startValue
    ).also {
        transparent = !transparent
    }
}

class SmoothAlphaAnimationHelper(
    target: View,
    duration: Long
) : LinearAnimationHelper(target, null, duration) {
    override fun evaluate() = (view.alpha == 1f && view.isDisplayed).let {
        animateFloat(
            "alpha",
            if (it) 1f else 0f,
            if (it) 0f else 1f,
            if (it) DecelerateInterpolator() else AccelerateInterpolator()
        )
    }
}

class RotationAnimationHelper(
    target: View,
    interpolator: TimeInterpolator = LinearInterpolator(),
    duration: Long
) : LinearAnimationHelper(target, interpolator, duration) {
    private var collapsed = true
    override fun evaluate() = animateInt(
        "rotation",
        if (collapsed) 180 else 90,
        if (collapsed) 90 else 180
    ).also {
        collapsed = !collapsed
    }
}

class PlainTranslationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {

    private var initX = view.left
    private var initY = view.bottom

    override fun evaluateXY() = Pair(
        animateFloat(
            "translationX",
            initX.toFloat() - view.left + view.translationX,
            0f
        ) as Animator,
        animateFloat(
            "translationY",
            initY.toFloat() - view.bottom + view.translationY,
            0f
        ) as Animator
    ).also {
        initX = view.left
        initY = view.bottom
    }
}

class ReturnScaleAnimationHelper(
    private val scaleTo: Float = 0.4f,
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {
    override fun evaluateXY() = (view.scaleX in (scaleTo - 0.1)..(scaleTo + 0.1) || view.scaleX == 1f).let {
        if (it) {
            Pair(
                animateFloat("scaleX", view.scaleX, scaleTo, returnTo = 1f),
                animateFloat("scaleY", view.scaleY, scaleTo, returnTo = 1f)
            )
        } else {
            Pair(
                animateFloat("scaleX", view.scaleX, 1f, AccelerateInterpolator(), duration * view.scaleX.toLong()),
                animateFloat("scaleY", view.scaleY, 1f, AccelerateInterpolator(), duration * view.scaleY.toLong())
            )
        }
    }
}