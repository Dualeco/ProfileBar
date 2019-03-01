package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.ContentFrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.dichotome.profilebar.R
import com.dichotome.profilebar.util.anim.*
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilebar.util.view.SquareRoundedImageView
import com.dichotome.profilebar.util.view.extensions.*
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

        private const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 250L
        private const val TRANSITION_THRESHOLD: Float = 0.52f
    }


    private var lastPosition = 0
    private var toolbarOpen = true
    private var constraintsChanged = false
    private var dimAdjusted = false
    private var minHeightSet = false
    private var animatorsInitialised = false

    private lateinit var appBar: ProfileBar

    private var zoomablePhotoLarge: SquareRoundedImageView? = null
    private var zoomablePhotoSmall: SquareRoundedImageView? = null

    private var zoomOverlayView = FrameLayout(context).apply {
        id = R.id.zoom_overlay
        layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
    }

    private val overlayBackgroundDark = View(context).apply {
        layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        )
        setOnClickListener { /* Keeps the views below unresponsive when this is visible */ }
        setBackgroundColor(col(context, R.color.colorBlack))
        zoomOverlayView.addView(this)
        isVisible = false
    }

    private val isOverlayVisible = { overlayBackgroundDark.isVisible }

    private var translationName: AnimationHelper? = null
    private var translationJoinedOn: AnimationHelper? = null
    private var translationFrame: AnimationHelper? = null
    private var alphaPhoto: AnimationHelper? = null
    private var scalePhoto: AnimationHelper? = null
    private var rotationOptionButton: AnimationHelper? = null
    private var alphaDim: AnimationHelper? = null

    private var detachLargePhotoOnClick: AnimationHelper? = null
    private var translationLargePhotoOnClick: AnimationHelper? = null
    private var zoomLargePhotoOnClick: AnimationHelper? = null
    private var revealLargeOnClick: AnimationHelper? = null

    private var detachSmallPhotoOnClick: AnimationHelper? = null
    private var translationSmallPhotoOnClick: AnimationHelper? = null
    private var zoomSmallPhotoOnClick: AnimationHelper? = null
    private var revealSmallOnClick: AnimationHelper? = null

    private var alphaZoomBackground = AlphaAnimationHelper(
        overlayBackgroundDark,
        DecelerateInterpolator(),
        DURATION_ZOOM,
        0f, 1f
    )

    private var collapseAnimators = ArrayList<AnimationHelper>()
    private var zoomLargeAnimators = ArrayList<AnimationHelper>()
    private var zoomSmallAnimators = ArrayList<AnimationHelper>()

    private fun areSimilar(firstValue: Int, secondValue: Int) = Math.abs(firstValue - secondValue) < 40
    private fun isPhotoImageUpToDate(image: SquareRoundedImageView?) = image?.let {
        val currentPhoto = copyPhotoImage()
        areSimilar(image.layoutParams.width, currentPhoto.layoutParams.width) &&
                areSimilar(image.layoutParams.height, currentPhoto.layoutParams.height)
    } ?: false

    private fun initPhotoLarge() =
        if (isPhotoImageUpToDate(zoomablePhotoLarge))
            zoomablePhotoLarge
        else {
            copyPhotoImage().apply {
                setOnClickListener {
                    zoomLargeAnimators.evaluateAll()
                }
                isVisible = false
            }.also {
                zoomOverlayView.removeView(zoomablePhotoLarge)
                zoomablePhotoLarge = zoomOverlayView.addAndGetView(it)

                invalidateZoomLarge()
                initZoomLarge()
            }
        }

    private fun initPhotoSmall() =
        if (isPhotoImageUpToDate(zoomablePhotoSmall))
            zoomablePhotoSmall
        else {
            copyPhotoImage().apply {
                setOnClickListener {
                    zoomSmallAnimators.evaluateAll()
                }
                isVisible = false
            }.also {
                zoomablePhotoSmall = zoomOverlayView.addAndGetView(it)
                initZoomSmall()
            }
        }

    init {
        photoImage.setOnClickListener {
            val photo = if (toolbarOpen) initPhotoLarge() else initPhotoSmall()
            photo?.callOnClick()

            setOnBackButtonClickedOnce(isOverlayVisible) {
                photo?.callOnClick()
            }
        }
    }

    private val fullSizeConstraintSet = ConstraintSet()
    private val collapsedConstraintSet = ConstraintSet()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (parent is ProfileBar) {
            appBar = parent as ProfileBar
            appBar.addOnOffsetChangedListener(this)

            rootView.findViewById<ContentFrameLayout>(android.R.id.content)
                .addView(zoomOverlayView)

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

    private fun invalidateZoomLarge() {
        zoomLargeAnimators.clear()

        detachLargePhotoOnClick = null
        zoomLargePhotoOnClick = null
        translationLargePhotoOnClick = null
        revealLargeOnClick = null
    }

    private fun initZoomLarge() {
        zoomablePhotoLarge?.also {
            detachLargePhotoOnClick ?: run {
                detachLargePhotoOnClick = DetachFromFrameAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_CORNERS
                ).addTo(zoomLargeAnimators)
            }
            zoomLargePhotoOnClick ?: run {
                zoomLargePhotoOnClick = ZoomAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomLargeAnimators)
            }
            translationLargePhotoOnClick ?: run {
                translationLargePhotoOnClick = ZoomTranslationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomLargeAnimators)
            }
            revealLargeOnClick ?: run {
                revealLargeOnClick = ZoomCircularRevealHelper(
                    it,
                    overlayBackgroundDark,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomLargeAnimators)
            }
            alphaZoomBackground.addTo(zoomLargeAnimators)
        }
    }

    private fun initZoomSmall() {
        zoomablePhotoSmall?.also {
            detachSmallPhotoOnClick ?: run {
                detachSmallPhotoOnClick = DetachFromFrameAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_CORNERS
                ).addTo(zoomSmallAnimators)

            }
            zoomSmallPhotoOnClick ?: run {
                zoomSmallPhotoOnClick = ZoomAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomSmallAnimators)
            }
            translationSmallPhotoOnClick ?: run {
                translationSmallPhotoOnClick = ZoomTranslationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomSmallAnimators)
            }
            revealSmallOnClick ?: run {
                revealSmallOnClick = ZoomCircularRevealHelper(
                    it,
                    overlayBackgroundDark,
                    DecelerateInterpolator(),
                    DURATION_ZOOM
                ).addTo(zoomSmallAnimators)
            }
            alphaZoomBackground.addTo(zoomSmallAnimators)
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
        zoomLargeAnimators.cancelAll()
        zoomSmallAnimators.cancelAll()
    }
}