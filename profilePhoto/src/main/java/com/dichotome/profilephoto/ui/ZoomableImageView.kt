package com.dichotome.profilephoto.ui

import android.content.Context
import android.util.AttributeSet
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
import com.dichotome.profileshared.views.SquareRoundedImageView

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SquareRoundedImageView(context, attrs, defStyle) {

    companion object {
        const val DURATION_ZOOM = 180L
        private const val DURATION_CORNERS = 230L
    }
    private val listeners = mutableListOf<(it: ZoomableImageView) -> Unit>()

    private var onZoom = {
        listeners.forEach { it(this) }
        zoomAnimators.evaluateAll()
    }

    fun addOnZoomListener(listener: (it: ZoomableImageView) -> Unit) {
        listeners.add(listener)
    }

    private var zoomablePhoto = SquareRoundedImageView(context).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(0, 0)
        setOnClickListener {
            onZoom()
        }
    }

    private val overlayBackgroundDark = View(context).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(col(R.color.colorBlack))
        setOnClickListener {
            /*intercepts touch events to the overlaid views*/
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
            revealOnClick = ZoomCircularRevealHelper(
                it,
                overlayBackgroundDark,
                DecelerateInterpolator(),
                it.cornerRadius,
                Constants(context).DISPLAY_HEIGHT,
                DURATION_CORNERS
            ).addTo(zoomAnimators)
            alphaOverlay.addTo(zoomAnimators)
        }
    }

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