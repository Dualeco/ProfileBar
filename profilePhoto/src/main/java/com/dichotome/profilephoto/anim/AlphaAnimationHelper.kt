package com.dichotome.profilephoto.anim

import android.animation.TimeInterpolator
import android.view.View
import com.dichotome.profileshared.anim.LinearAnimationHelper
import kotlin.math.max

class AlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long,
    private val startValue: Float = 1f,
    private val endValue: Float
) : LinearAnimationHelper(target, interpolator, duration) {

    override fun evaluate() = (view.alpha < max(startValue, endValue)).let {
        animateFloat(
            "alpha",
            if (it) startValue else endValue,
            if (it) endValue else startValue
        )
    }
}