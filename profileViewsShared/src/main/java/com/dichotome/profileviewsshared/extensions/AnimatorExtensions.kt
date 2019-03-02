package com.dichotome.profileviewsshared.extensions

import android.animation.ValueAnimator
import android.view.ViewPropertyAnimator
import com.dichotome.profileviewsshared.anim.AnimationHelper
import com.dichotome.profileviewsshared.anim.LinearAnimationHelper
import com.dichotome.profileviewsshared.anim.PlainAnimationHelper
import com.dichotome.profileviewsshared.anim.SimpleAnimationHelper


fun ViewPropertyAnimator.addTo(collection: MutableCollection<ViewPropertyAnimator>) = apply {
    collection.add(this)
}

fun <V : ValueAnimator> V.addTo(collection: MutableCollection<in V>) = apply {
    collection.add(this)
}

fun <H : AnimationHelper> H.addTo(collection: MutableCollection<in H>) = apply {
    collection.add(this)
}

fun Collection<AnimationHelper>.evaluateAll() = forEach {
    it.cancel()
    when (it) {
        is SimpleAnimationHelper -> it.evaluate()
        is LinearAnimationHelper -> it.evaluate()
        is PlainAnimationHelper -> it.evaluateXY()
    }
}

fun Collection<AnimationHelper?>.cancelAll() = forEach {
    it?.cancel()
}