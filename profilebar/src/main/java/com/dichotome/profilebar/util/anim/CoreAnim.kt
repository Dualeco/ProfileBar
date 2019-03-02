package com.dichotome.profilebar.util.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import androidx.core.util.Pair
import com.dichotome.profilebar.util.view.extensions.addTo

class DecelerateAccelerateInterpolator : TimeInterpolator {
    override fun getInterpolation(t: Float): Float {
        val y = 2 * t - 1
        val powY = Math.pow(y.toDouble(), 7.0)

        return (powY + 1).toFloat() / 2
    }
}

abstract class AnimationHelper(
    protected val view: View,
    private val interp: TimeInterpolator?,
    protected val duration: Long
) {
    private var animators = ArrayList<ObjectAnimator>()
    fun animateFloat(
        propertyName: String,
        from: Float,
        to: Float,
        animInterpolator: TimeInterpolator? = interp,
        dur: Long = duration,
        returnTo: Float? = null,
        autoStart: Boolean = true
    ): ObjectAnimator? {
        var path = floatArrayOf(from, to)
        returnTo?.let {
            path += returnTo
        }
        return ObjectAnimator.ofFloat(view, propertyName, *path)
            .apply {
                interpolator = animInterpolator
                duration = dur
                if (autoStart) start()
            }.addTo(animators)

    }

    fun animateInt(
        propertyName: String,
        from: Int,
        to: Int,
        animInterpolator: TimeInterpolator? = interp,
        dur: Long = duration,
        returnTo: Int? = null,
        autoStart: Boolean = true
    ): ObjectAnimator? {
        var path = intArrayOf(from, to)
        returnTo?.let { path += returnTo }
        return ObjectAnimator.ofInt(view, propertyName, *path)
            .apply {
                interpolator = animInterpolator
                duration = dur
                if (autoStart) start()
            }.addTo(animators)

    }

    fun cancel() {
        val cancellable = animators
        animators = arrayListOf()
        cancellable.forEach {
            it.cancel()
        }
    }
}

abstract class SimpleAnimationHelper(view: View, interpolator: TimeInterpolator, duration: Long) :
    AnimationHelper(view, interpolator, duration) {
    abstract fun evaluate(): Animator?
}

abstract class LinearAnimationHelper(
    view: View,
    interpolator: TimeInterpolator?,
    duration: Long
) : AnimationHelper(view, interpolator, duration) {
    abstract fun evaluate(): Animator?
}

abstract class PlainAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : AnimationHelper(target, interpolator, duration) {
    abstract fun evaluateXY(): Pair<Animator?, Animator?>
}
