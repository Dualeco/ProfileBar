package com.dichotome.profileviewsshared.anim

import android.animation.TimeInterpolator
import android.view.View

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