package com.dichotome.profilephoto.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.dichotome.profilephoto.R
import com.dichotome.profilephoto.anim.DetachFromFrameAnimationHelper
import com.dichotome.profilephoto.anim.ZoomAnimationHelper
import com.dichotome.profilephoto.anim.ZoomCircularRevealHelper
import com.dichotome.profilephoto.anim.ZoomTranslationHelper
import com.dichotome.profilephoto.util.extensions.copyForOverlay
import com.dichotome.profilephoto.util.extensions.setInCenter
import com.dichotome.profilephoto.util.extensions.setOnBackButtonClicked
import com.dichotome.profilephoto.anim.AlphaAnimationHelper
import com.dichotome.profileshared.anim.AnimationHelper
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.addTo
import com.dichotome.profileshared.extensions.cancelAll
import com.dichotome.profileshared.extensions.col
import com.dichotome.profileshared.extensions.evaluateAll
import com.dichotome.profileshared.gestureListeners.SwipeUpListener
import com.dichotome.profileshared.views.CircularImageView
import kotlin.math.max

class ZoomingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CircularImageView(context, attrs, defStyle) {

    companion object {
        const val TAG = "ZoomingImageView"

        const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 250L

        private const val SUPER_STATE = "superState"
        private const val PHOTO_WIDTH = "photoWidth"
        private const val PHOTO_HEIGHT = "photoWidth"
        private const val IS_ZOOMING_PHOTO_VISIBLE = "isZoomingPhotoVisible"
        private const val IS_PHOTO_VIEW_VISIBLE = "isPhotoViewVisible"
        private const val OVERLAY_ALPHA = "overlayAlpha"
        private const val SCALE = "scale"
    }

    private var goInvisible = false

    private val displayHeight = Constants(context).DISPLAY_HEIGHT
    private val displayWidth = Constants(context).DISPLAY_WIDTH

    private val photoZoomedSize = max(displayHeight, displayWidth)

    private var onZoomListener: ((it: ZoomingImageView) -> Unit)? = null

    fun setOnZoomListener(listener: (it: ZoomingImageView) -> Unit) {
        onZoomListener = listener
    }

    private fun onZoom() {
        if (wasViewRestored) restorePhoto()

        onZoomListener?.invoke(this)
        zoomAnimators.evaluateAll()
    }

    private var swipeUpDetector = GestureDetector(context, SwipeUpListener(this) {
        onZoom()
    })

    private var zoomablePhoto = CircularImageView(context).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(0, 0)
        setOnTouchListener { _, event ->
            swipeUpDetector.onTouchEvent(event)
            true
        }
    }

    private val overlayBackgroundDark = View(context).apply {
        alpha = 0f
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(col(R.color.colorBlack))
        setOnTouchListener { v, event ->
            swipeUpDetector.onTouchEvent(event)
            true
        }
    }

    private fun isOverlayVisible() = overlayBackgroundDark.isVisible

    private var zoomOverlayView = FrameLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER
        }
        addView(overlayBackgroundDark)
        addView(zoomablePhoto)
    }

    private val zoomAnimators = ArrayList<AnimationHelper>()
    private var detachOnClick: AnimationHelper? = null
    private var translationOnClick: AnimationHelper? = null
    private var zoomOnClick: AnimationHelper? = null
    private var revealOnClick: AnimationHelper? = null

    private var alphaOverlay = AlphaAnimationHelper(
        overlayBackgroundDark,
        DecelerateInterpolator(),
        DURATION_ZOOM,
        0f, 1f
    )

    private var wasViewRestored = false

    private fun isPhotoImageUpToDate(image: CircularImageView?) = image?.let {
        val currentPhoto = CircularImageView(context)
            .copyForOverlay(this)

        Math.abs(image.y - currentPhoto.y) < 10
    } ?: false

    private fun initPhoto() = zoomablePhoto.let {
        if (!isPhotoImageUpToDate(it)) {
            it.copyForOverlay(this)

            initZoom()
        }
    }

    private fun restorePhoto() {
        initPhoto()
        zoomablePhoto.apply { setInCenter() }

        wasViewRestored = false
    }

    private fun initZoom() {
        zoomablePhoto.also {
            zoomAnimators.clear()

            detachOnClick = DetachFromFrameAnimationHelper(
                it,
                this,
                DecelerateInterpolator(),
                DURATION_CORNERS
            ).addTo(zoomAnimators)

            zoomOnClick = ZoomAnimationHelper(
                it,
                DecelerateInterpolator(),
                DURATION_ZOOM
            ).addTo(zoomAnimators)

            translationOnClick = ZoomTranslationHelper(
                it,
                DecelerateInterpolator(),
                DURATION_ZOOM
            ).addTo(zoomAnimators)

            revealOnClick = ZoomCircularRevealHelper(
                it,
                overlayBackgroundDark,
                DecelerateInterpolator(),
                it.cornerRadius,
                photoZoomedSize,
                DURATION_CORNERS
            ).addTo(zoomAnimators)

            alphaOverlay.addTo(zoomAnimators)
        }
    }

    override fun makeCircular() = also { super.makeCircular() }

    init {
        setOnClickListener {
            wasViewRestored = false
            initPhoto()
            onZoom()
        }
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(SUPER_STATE, super.onSaveInstanceState())

        zoomablePhoto.apply {
            layoutParams.apply {
                putInt(PHOTO_WIDTH, width)
                putInt(PHOTO_HEIGHT, height)
            }
            putBoolean(IS_ZOOMING_PHOTO_VISIBLE, isVisible)
            putFloat(SCALE, scaleX)
        }

        putBoolean(IS_PHOTO_VIEW_VISIBLE, isVisible)

        overlayBackgroundDark.apply {
            putFloat(OVERLAY_ALPHA, alpha)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val superState = state?.let {
            it as Bundle
            overlayBackgroundDark.apply {
                isVisible = it.getBoolean(IS_ZOOMING_PHOTO_VISIBLE)
                alpha = it.getFloat(OVERLAY_ALPHA)
            }

            zoomablePhoto.apply {
                layoutParams.apply {
                    width = it.getInt(PHOTO_WIDTH)
                    height = it.getInt(PHOTO_HEIGHT)
                }
                isVisible = it.getBoolean(IS_ZOOMING_PHOTO_VISIBLE)
                scaleX = it.getFloat(SCALE)
                scaleY = it.getFloat(SCALE)

                setInCenter()
            }

            wasViewRestored = true

            it.getParcelable<Parcelable>(SUPER_STATE)
        }

        super.onRestoreInstanceState(superState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        rootView.findViewById<FrameLayout>(android.R.id.content).apply {
            addView(zoomOverlayView)
            setOnBackButtonClicked(::isOverlayVisible) {
                onZoom()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        drawable?.let {
            zoomablePhoto.setImageDrawable(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        zoomAnimators.cancelAll()
    }
}