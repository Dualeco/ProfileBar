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
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.dichotome.profilephoto.R
import com.dichotome.profilephoto.anim.*
import com.dichotome.profileshared.anim.AlphaAnimationHelper
import com.dichotome.profileshared.anim.AnimationHelper
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.*
import com.dichotome.profileshared.gestureListeners.SwipeUpListener
import com.dichotome.profileshared.views.SquareRoundedImageView

class ZoomingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SquareRoundedImageView(context, attrs, defStyle) {

    companion object {
        const val TAG = "ZoomingImageView"

        const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 250L

        private const val SUPER_STATE = "superState"
        private const val PHOTO_WIDTH = "photoWidth"
        private const val PHOTO_HEIGHT = "photoWidth"
        private const val PHOTO_BITMAP = "photoDrawable"
        private const val IS_PHOTO_VISIBLE = "isPhotoVisible"
        private const val OVERLAY_ALPHA = "overlayAlpha"
        private const val SCALE = "scale"
    }

    private val displayHeight = Constants(context).DISPLAY_HEIGHT
    private val displayWidth = Constants(context).DISPLAY_WIDTH

    private fun max(a: Int, b: Int) = if (a > b) a else b

    private val photoZoomedSize = max(displayHeight, displayWidth)

    private var onZoomListener: ((it: ZoomingImageView) -> Unit)? = null

    fun setOnZoomListener(listener: (it: ZoomingImageView) -> Unit) {
        onZoomListener = listener
    }

    private fun onZoom() {
        if (!isPhotoAdjusted) {
            initPhoto()
            isPhotoAdjusted = true
        }
        onZoomListener?.invoke(this)
        zoomAnimators.evaluateAll()
    }

    private var swipeUpDetector = GestureDetector(context, SwipeUpListener(this) {
        onZoom()
    })

    private var zoomablePhoto = SquareRoundedImageView(context).apply {
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

    private val isOverlayVisible = { overlayBackgroundDark.isVisible }

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

    private var isPhotoAdjusted = true

    private fun isPhotoImageUpToDate(image: SquareRoundedImageView?) = image?.let {
        val currentPhoto = SquareRoundedImageView(context).copyForOverlay(this)
        Math.abs(image.y - currentPhoto.y) < 10
    } ?: false

    private fun initPhoto() = zoomablePhoto.let {
        if (!isPhotoImageUpToDate(it)) {
            it.copyForOverlay(this)
            isPhotoAdjusted = false
            initZoom()
        }
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
            isPhotoAdjusted = false
            onZoom()
        }
    }

    override fun onSaveInstanceState() = Bundle().apply {
        zoomablePhoto.apply {
            putParcelable(SUPER_STATE, super.onSaveInstanceState())
            putInt(PHOTO_WIDTH, layoutParams.width)
            putInt(PHOTO_HEIGHT, layoutParams.height)
            drawable?.let {
                putParcelable(PHOTO_BITMAP, it.toBitmap())
            }
            putBoolean(IS_PHOTO_VISIBLE, isVisible)
            putFloat(SCALE, scaleX)
        }
        putFloat(OVERLAY_ALPHA, overlayBackgroundDark.alpha)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        val superState = (state as? Bundle)?.let {
            overlayBackgroundDark.apply {
                isVisible = it.getBoolean(IS_PHOTO_VISIBLE)
                alpha = it.getFloat(OVERLAY_ALPHA)
            }
            zoomablePhoto.apply {
                isVisible = it.getBoolean(IS_PHOTO_VISIBLE)

                layoutParams.apply {
                    width = it.getInt(PHOTO_WIDTH)
                    height = it.getInt(PHOTO_HEIGHT)
                }
                x = getViewCenteredX(this)
                y = getViewCenteredY(this)
                setImageBitmap(it.getParcelable(PHOTO_BITMAP))

                scaleX = it.getFloat(SCALE)
                scaleY = it.getFloat(SCALE)
            }

            isPhotoAdjusted = false
            val superState = it.getParcelable<Parcelable>(SUPER_STATE)
            it.clear()

            superState
        }

        super.onRestoreInstanceState(superState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        rootView.findViewById<FrameLayout>(android.R.id.content).apply {
            addView(zoomOverlayView)
            setOnBackButtonClicked(isOverlayVisible) {
                onZoom()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        zoomAnimators.cancelAll()
    }
}