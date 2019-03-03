package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import com.dichotome.profilebar.R
import com.dichotome.profilebar.util.anim.*
import com.dichotome.profileshared.anim.AnimationHelper
import com.dichotome.profileshared.anim.DecelerateAccelerateInterpolator
import com.dichotome.profileshared.extensions.addTo
import com.dichotome.profileshared.extensions.cancelAll
import com.dichotome.profileshared.extensions.evaluateAll
import com.google.android.material.appbar.AppBarLayout

class ProfileCollapsingToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ProfileToolbar(context, attrs, defStyle),
    AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val TAG = "ToolbarCollapsing"

        private const val DURATION = 550L
        private const val DURATION_MEDIUM = (0.95 * DURATION).toLong()
        private const val DURATION_SHORT = (0.9 * DURATION).toLong()
        private const val DURATION_SHORTER = (0.85 * DURATION).toLong()

        private const val TRANSITION_THRESHOLD: Float = 0.52f
    }

    private var lastPosition = 0
    private var toolbarOpen = true
    private var constraintsChanged = false
    private var dimAdjusted = false
    private var minHeightSet = false
    private var animatorsInitialised = false

    private lateinit var appBar: ProfileBar

    private var collapseAnimators = ArrayList<AnimationHelper>()

    private var translationName: AnimationHelper? = null
    private var translationJoinedOn: AnimationHelper? = null
    private var translationFrame: AnimationHelper? = null
    private var alphaPhoto: AnimationHelper? = null
    private var scalePhoto: AnimationHelper? = null
    private var rotationOptionButton: AnimationHelper? = null
    private var alphaDim: AnimationHelper? = null

    private val fullSizeConstraintSet = ConstraintSet()
    private val collapsedConstraintSet = ConstraintSet()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (parent is ProfileBar) {
            appBar = parent as ProfileBar
            appBar.addOnOffsetChangedListener(this)

            fullSizeConstraintSet.clone(context, R.layout.toolbar_profile)
            collapsedConstraintSet.clone(context, R.layout.toolbar_profile_collapsed)

            fullSizeConstraintSet.applyTo(this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (constraintsChanged) {
            dimAdjusted = false
            collapseAnimators.evaluateAll()

            constraintsChanged = false
        }

        if (appBar.measuredHeight > 0) {

            val dimHeight = (appBar.measuredHeight * (1 - TRANSITION_THRESHOLD)).toInt()
            if (!dimAdjusted) {
                dimView.layoutParams = dimView.layoutParams.apply {
                    height = dimHeight
                }
                dimAdjusted = true
            }
            if (!minHeightSet) {
                minimumHeight = dimHeight
                minHeightSet = true
            }
        }
    }

    private fun initCollapseAnimators() {
        titleTV.let {
            translationName ?: run {
                translationName = HorizontalAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION
                ).addTo(collapseAnimators)
            }
        }
        subtitleTV.let {
            translationJoinedOn ?: run {
                translationJoinedOn = HorizontalAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION
                ).addTo(collapseAnimators)
            }
        }
        photoFrame.let {
            translationFrame ?: run {
                translationFrame = PlainTranslationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_SHORTER
                ).addTo(collapseAnimators)
            }
            alphaPhoto ?: run {
                alphaPhoto = ReturnAlphaAnimationHelper(
                    it,
                    DecelerateAccelerateInterpolator(),
                    DURATION_MEDIUM
                ).addTo(collapseAnimators)
            }
            scalePhoto ?: run {
                scalePhoto =
                    ReturnScaleAnimationHelper(
                        0.4f,
                        it,
                        DecelerateAccelerateInterpolator(),
                        DURATION_MEDIUM
                    ).addTo(collapseAnimators)
            }
        }
        optionButton.let {
            rotationOptionButton ?: run {
                rotationOptionButton = RotationAnimationHelper(
                    it,
                    OvershootInterpolator(),
                    DURATION_SHORT
                ).addTo(collapseAnimators)
            }
        }
        dimView.let {
            alphaDim ?: run {
                alphaDim = AlphaAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_SHORT,
                    1f, 0.8f
                ).addTo(collapseAnimators)
            }
        }
    }

    override fun onOffsetChanged(appBar: AppBarLayout?, verticalOffset: Int) {
        appBar?.let {
            if (lastPosition == verticalOffset) {
                return
            }
            lastPosition = verticalOffset

            if (toolbarOpen) {
                layoutParams = (layoutParams as AppBarLayout.LayoutParams).apply {
                    topMargin = -verticalOffset
                }
            }
            val progress = Math.abs(verticalOffset / it.height.toFloat())

            if (toolbarOpen && progress > TRANSITION_THRESHOLD) {
                if (!animatorsInitialised)
                    initCollapseAnimators()

                collapsedConstraintSet.applyTo(this)
                constraintsChanged = true
                toolbarOpen = false

            } else if (!toolbarOpen && progress < TRANSITION_THRESHOLD) {
                fullSizeConstraintSet.applyTo(this)
                constraintsChanged = true
                toolbarOpen = true
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        collapseAnimators.cancelAll()
    }
}