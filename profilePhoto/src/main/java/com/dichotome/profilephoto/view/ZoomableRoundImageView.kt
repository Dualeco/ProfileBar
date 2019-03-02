package com.dichotome.profilephoto.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilephoto.R
import com.dichotome.profilephoto.anim.DetachFromFrameAnimationHelper
import com.dichotome.profilephoto.anim.ZoomAnimationHelper
import com.dichotome.profilephoto.anim.ZoomCircularRevealHelper
import com.dichotome.profilephoto.anim.ZoomTranslationHelper
import com.dichotome.profileviewsshared.anim.AlphaAnimationHelper
import com.dichotome.profileviewsshared.anim.AnimationHelper
import com.dichotome.profileviewsshared.constants.Constants
import com.dichotome.profileviewsshared.extensions.*
import com.dichotome.profileviewsshared.views.SquareRoundedImageView

class ZoomableRoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SquareRoundedImageView(context, attrs, defStyle) {

    companion object {
        private const val DURATION_ZOOM = 200L
        private const val DURATION_CORNERS = 250L

    }

    private fun onZoomedPhotoClicked() = zoomAnimators.evaluateAll()

    private var zoomablePhoto = SquareRoundedImageView(context).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(0, 0)
        setOnClickListener {
            Toast.makeText(context, "clickZoomable", Toast.LENGTH_SHORT).show()
            onZoomedPhotoClicked()
        }
    }

    private val overlayBackgroundDark = View(context).apply {
        isVisible = false
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(col(context, R.color.colorBlack))
        setOnClickListener {
            onZoomedPhotoClicked()
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

    private fun areAdjacent(firstValue: Float, secondValue: Float) = Math.abs(firstValue - secondValue) < 10
    private fun isPhotoImageUpToDate(image: SquareRoundedImageView?) = image?.let {
        val currentPhoto = SquareRoundedImageView(context).copyForOverlay(this)
        areAdjacent(image.layoutParams.width.toFloat(), currentPhoto.layoutParams.width.toFloat()) &&
                areAdjacent(image.y, currentPhoto.y)
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
        makeCircular()
        setOnClickListener {
            initPhoto()
            onZoomedPhotoClicked()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        rootView.findViewById<FrameLayout>(android.R.id.content).apply {
            addView(zoomOverlayView)
            setOnBackButtonClicked(isOverlayVisible) {
                onZoomedPhotoClicked()
            }
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        zoomAnimators.cancelAll()
    }
}