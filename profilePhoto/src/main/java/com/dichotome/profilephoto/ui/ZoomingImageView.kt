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
import androidx.core.os.bundleOf
import com.dichotome.profilephoto.R
import com.dichotome.profilephoto.anim.*
import com.dichotome.profilephoto.util.extensions.copyForOverlay
import com.dichotome.profilephoto.util.extensions.setInCenter
import com.dichotome.profilephoto.util.extensions.setOnBackButtonClicked
import com.dichotome.profileshared.anim.AnimationHelper
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.*
import com.dichotome.profileshared.gestureListeners.SwipeUpListener
import com.dichotome.profileshared.views.RoundedImageView
import kotlin.math.max

class ZoomingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RoundedImageView(context, attrs, defStyle) {

    companion object {

        const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 220L

        private const val SUPER_STATE = "superState"
        private const val PHOTO_WIDTH = "photoWidth"
        private const val PHOTO_HEIGHT = "photoHeight"
        private const val IS_ZOOMING_PHOTO_VISIBLE = "isZoomingPhotoVisible"
        private const val IS_PHOTO_VIEW_VISIBLE = "isPhotoViewVisible"
        private const val OVERLAY_ALPHA = "overlayAlpha"
        private const val SCALE = "scale"
    }

    private var isOverlayAttached = false

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
        zoomAnimators.endAll()
        zoomAnimators.evaluateAll()
    }

    private fun zoomOut() {
        if (isZoomed) {
            isZoomed = false
            onZoom()
        }
    }

    private fun zoomIn() {
        if (!isZoomed) {
            isZoomed = true
            onZoom()
        }
    }

    private var zoomablePhoto: RoundedImageView = RoundedImageView(context).apply {
        isDisplayed = false
        isSquare = this@ZoomingImageView.isSquare
        isCircular = this@ZoomingImageView.isCircular
        layoutParams = FrameLayout.LayoutParams(0, 0)
        setOnTouchListener { _, event ->
            swipeUpDetector.onTouchEvent(event)
            true
        }
    }

    private var isZoomed = false

    private var swipeUpDetector = GestureDetector(context, SwipeUpListener(this) {
        zoomOut()
    })

    private val overlayBackgroundDark = View(context).apply {
        alpha = 0f
        isDisplayed = false
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

    private fun isOverlayVisible() = overlayBackgroundDark.isDisplayed

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

    private fun isPhotoImageUpToDate(image: RoundedImageView?) = image?.let {
        val currentPhoto = RoundedImageView(context)
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


            //
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

    init {
        setOnClickListener {
            wasViewRestored = false
            initPhoto()
            zoomIn()
        }
    }

    override fun onSaveInstanceState() = zoomablePhoto.let {
        bundleOf(
            SUPER_STATE to super.onSaveInstanceState(),
            IS_PHOTO_VIEW_VISIBLE to this@ZoomingImageView.isDisplayed,
            IS_ZOOMING_PHOTO_VISIBLE to it.isDisplayed,
            PHOTO_HEIGHT to it.layoutParams.height,
            PHOTO_WIDTH to it.layoutParams.width,
            SCALE to it.scaleX,
            OVERLAY_ALPHA to overlayBackgroundDark.alpha
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle)?.run {
            super.onRestoreInstanceState(getParcelable<Parcelable>(SUPER_STATE))

            overlayBackgroundDark.apply {
                isDisplayed = getBoolean(IS_ZOOMING_PHOTO_VISIBLE)
                alpha = getFloat(OVERLAY_ALPHA)
            }

            zoomablePhoto.apply {
                layoutParams.width = getInt(PHOTO_WIDTH)
                layoutParams.height = getInt(PHOTO_HEIGHT)
                isDisplayed = getBoolean(IS_ZOOMING_PHOTO_VISIBLE)
                scaleX = getFloat(SCALE)
                scaleY = getFloat(SCALE)

                setInCenter()
            }

            wasViewRestored = true
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (!isOverlayAttached)
            rootView.findViewById<FrameLayout>(android.R.id.content).apply {
                addView(zoomOverlayView)
                setOnBackButtonClicked(::isOverlayVisible) {
                    zoomIn()
                }
                isOverlayAttached = true
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