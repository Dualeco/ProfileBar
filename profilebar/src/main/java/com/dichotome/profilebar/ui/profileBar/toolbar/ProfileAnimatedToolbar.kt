package com.dichotome.profilebar.ui.profileBar.toolbar

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.profileBar.ProfileBar
import com.dichotome.profilebar.util.anim.*
import com.dichotome.profilephoto.ui.ZoomingImageView
import com.dichotome.profileshared.anim.AnimationHelper
import com.dichotome.profileshared.anim.DecelerateAccelerateInterpolator
import com.dichotome.profileshared.extensions.addTo
import com.dichotome.profileshared.extensions.cancelAll
import com.dichotome.profileshared.extensions.evaluateAll
import com.google.android.material.appbar.AppBarLayout

class ProfileAnimatedToolbar @JvmOverloads constructor(
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

        private const val SUPER_STATE = "superState"
        private const val FRAME_BACKGROUND_ALPHA = "frameBackgroundAlpha"

        private var TRANSITION_THRESHOLD: Float = 0.52f
    }

    private var lastPosition = 0
    private var toolbarOpen = true
    private var constraintsChanged = false
    private var dimAdjusted = false
    private var animatorsInitialised = false

    private lateinit var appBar: ProfileBar

    private var collapseAnimators = ArrayList<AnimationHelper>()

    private lateinit var translationName: AnimationHelper
    private lateinit var translationJoinedOn: AnimationHelper
    private lateinit var translationFrame: AnimationHelper
    private lateinit var alphaPhoto: AnimationHelper
    private lateinit var scalePhoto: AnimationHelper
    private lateinit var rotationOptionButton: AnimationHelper
    private lateinit var alphaDim: AnimationHelper

    private lateinit var alphaPhotoFrameBackground: AnimationHelper

    private val fullSizeConstraintSet = ConstraintSet()
    private val collapsedConstraintSet = ConstraintSet()

    init {
        id = R.id.toolbar_animated
        initFrameTransparency()
    }

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
            if (!dimAdjusted) {
                val appBarHeight = appBar.measuredHeight.toFloat()
                val dimHeight = tabs.measuredHeight + appBarHeight / 3
                TRANSITION_THRESHOLD = 1f - (dimHeight / appBarHeight)

                dimView.layoutParams = dimView.layoutParams.apply {
                    height = dimHeight.toInt()
                }
                dimAdjusted = true
                minimumHeight = dimHeight.toInt()
            }
        }
    }

    private fun initFrameTransparency() {

        alphaPhotoFrameBackground = SmoothAlphaAnimationHelper(
            photoFrameBackground,
            ZoomingImageView.DURATION_ZOOM
        ).apply {
            photoImage.setOnZoomListener {
                evaluate()
            }
        }
    }

    private fun initAnimators() {
        titleTV.let {
            translationName = HorizontalAnimationHelper(
                it,
                DecelerateInterpolator(),
                DURATION
            ).addTo(collapseAnimators)
        }

        subtitleTV.let {
            translationJoinedOn = HorizontalAnimationHelper(
                it,
                DecelerateInterpolator(),
                DURATION
            ).addTo(collapseAnimators)
        }

        photoFrame.let {
            translationFrame = PlainTranslationHelper(
                it,
                DecelerateInterpolator(),
                DURATION_SHORTER
            ).addTo(collapseAnimators)

            alphaPhoto = ReturnAlphaAnimationHelper(
                it,
                DecelerateAccelerateInterpolator(),
                DURATION_MEDIUM
            ).addTo(collapseAnimators)

            scalePhoto = ReturnScaleAnimationHelper(
                0.4f,
                it,
                DecelerateAccelerateInterpolator(),
                DURATION_MEDIUM
            ).addTo(collapseAnimators)
        }

        optionButton.let {
            rotationOptionButton = RotationAnimationHelper(
                it,
                OvershootInterpolator(),
                DURATION_SHORT
            ).addTo(collapseAnimators)
        }

        dimView.let {
            alphaDim = AlphaAnimationHelper(
                it,
                DecelerateInterpolator(),
                DURATION_SHORT,
                1f, 0.8f
            ).addTo(collapseAnimators)
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

            if (toolbarOpen && progress >= TRANSITION_THRESHOLD) {
                if (!animatorsInitialised) {
                    initAnimators()
                    animatorsInitialised = true
                }

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

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(SUPER_STATE, super.onSaveInstanceState())
        putFloat(FRAME_BACKGROUND_ALPHA, photoFrameBackground.alpha)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        val superState = state?.let { it as Bundle
            photoFrameBackground.alpha = it.getFloat(FRAME_BACKGROUND_ALPHA)

            it.getParcelable<Parcelable>(SUPER_STATE)
        }

        super.onRestoreInstanceState(superState)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        collapseAnimators.cancelAll()
        alphaPhotoFrameBackground.cancel()
    }
}