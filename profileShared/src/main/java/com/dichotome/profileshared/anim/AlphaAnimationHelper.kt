package com.dichotome.profileshared.anim

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.util.Log
import android.view.View

class AlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long,
    private val startValue: Float = 1f,
    private val endValue: Float
) : LinearAnimationHelper(target, interpolator, duration) {
    override fun evaluate(): ObjectAnimator? {
        Log.d("ZoomingImageView", "${view.alpha}")
        val collapsed = view.alpha == 0f

        return animateFloat(
            "alpha",
            if (collapsed) startValue else endValue,
            if (collapsed) endValue else startValue
        )
    }
}