package com.dichotome.profilephoto.ui

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.dichotome.profilephoto.R
import com.dichotome.profilephoto.anim.DetachFromFrameAnimationHelper
import com.dichotome.profilephoto.anim.ZoomAnimationHelper
import com.dichotome.profilephoto.anim.ZoomCircularRevealHelper
import com.dichotome.profilephoto.anim.ZoomTranslationHelper
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
        const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 250L
    }

    private var onZoomListener: ((it: ZoomingImageView) -> Unit)? = null

    fun setOnZoomListener(listener: (it: ZoomingImageView) -> Unit) {
        onZoomListener = listener
    }

    private fun onZoom() {
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
            swipeUpDetector?.onTouchEvent(event)
            true
        }
    }

    private val overlayBackgroundDark = View(context).apply {
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
        )
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

    private fun isPhotoImageUpToDate(image: SquareRoundedImageView?) = image?.let {
        val currentPhoto = SquareRoundedImageView(context).copyForOverlay(this)
        Math.abs(image.y - currentPhoto.y) < 10
    } ?: false

    private fun initPhoto() = zoomablePhoto.let {
        if (!isPhotoImageUpToDate(it)) {
            it.copyForOverlay(this)
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




            val dispHeight = Constants(context).DISPLAY_HEIGHT
            val dispWidth = Constants(context).DISPLAY_WIDTH
            revealOnClick = ZoomCircularRevealHelper(
                it,
                overlayBackgroundDark,
                DecelerateInterpolator(),
                it.cornerRadius,
                max(dispHeight, dispWidth),
                DURATION_CORNERS
            ).addTo(zoomAnimators)
            alphaOverlay.addTo(zoomAnimators)
        }
    }
    private fun max(a: Int, b: Int) = if (a > b) a else b

    init {
        setOnClickListener {
            initPhoto()
            onZoom()
        }
    }

    override fun makeCircular() = also { super.makeCircular() }

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