package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.ViewPager
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilebar.util.constant.dpToPx
import com.dichotome.profilebar.util.constant.drw
import com.dichotome.profilebar.util.view.SquareImageView
import com.dichotome.profilebar.util.view.extensions.*
import com.google.android.material.appbar.AppBarLayout

class ProfileCollapsingToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ConstraintLayout(context, attrs, defStyle),
    AppBarLayout.OnOffsetChangedListener,
    ProfileBarInterface {

    companion object {
        const val TAG = "Toolbar"

        private const val DURATION = 550L
        private const val DURATION_MEDIUM = (0.95 * DURATION).toLong()
        private const val DURATION_SHORT = (0.9 * DURATION).toLong()
        private const val DURATION_SHORTER = (0.85 * DURATION).toLong()
        private const val TRANSITION_THRESHOLD: Float = 0.52f
        private val DEFAULT_TEXT_COLOR_ID = R.color.colorPrimary
        private const val DEFAULT_TITLE_TEXT_SIZE = 20f
        private const val DEFAULT_SUBTITLE_TITLE_TEXT_SIZE = 12f
        private val DEFAULT_FRAME_DRAWABLE_ID = R.drawable.profile_photo_stroke
        private const val DEFAULT_FRAME_THICKNESS_DP = 2
        private val DEFAULT_FRAME_COLOR_ID = R.color.colorPrimaryTranslucent
        private val DEFAULT_DIM_ID = R.drawable.backdround_dim_gradient_dark
        private val DEFAULT_BOTTOM_GLOW_ID = R.drawable.bottom_glow
        private val DEFAULT_TABS_SELECTED_COLOR_ID = R.color.colorPrimary
        private val DEFAULT_TABS_UNSELECTED_COLOR_ID = R.color.colorPrimaryUnselected
        private val DEFAULT_PHOTO_ID = R.drawable.profile_photo_stub
    }

    private lateinit var appBar: AppBarLayout

    private var lastPosition = 0
    private var toolbarOpen = true
    private var constraintsChanged = false
    private var dimAdjusted = false
    private var minHeightSet = false
    private var animatorsInitialised = false

    override var tabsEnabled: Boolean = false
        set(value) {
            field = value
            tabs.enabled = field
        }

    override var tabsSelectedColor: Int = col(context, DEFAULT_TABS_SELECTED_COLOR_ID)
        set(value) {
            field = value
            tabs.setTabTextColors(tabsUnselectedColor, field)
        }

    override var tabsUnselectedColor: Int = col(context, DEFAULT_TABS_UNSELECTED_COLOR_ID)
        set(value) {
            field = value
            tabs.setTabTextColors(field, tabsSelectedColor)
        }

    override var tabsIndicatorColor: Int = col(context, DEFAULT_TABS_SELECTED_COLOR_ID)
        set (value) {
            field = value
            tabs.setSelectedTabIndicatorColor(field)
        }

    override var wallpaperDrawable: Drawable? = null
        set(value) {
            field = value
            wallpaperImage.setImageDrawable(field)
        }

    override var photoDrawable: Drawable? = drw(context, DEFAULT_PHOTO_ID)
        set(value) {
            field = value
            photoImage.download(field, true)
        }

    override var fontColor: Int = col(context, DEFAULT_TEXT_COLOR_ID)
        set(value) {
            field = value
            titleTV.setTextColor(value)
            subtitleTV.setTextColor(value)
        }

    override var titleText: String? = null
        set(value) {
            field = value
            titleTV.text = field
        }

    override var titleTextSize: Float =
        DEFAULT_TITLE_TEXT_SIZE
        set(value) {
            field = value
            titleTV.textSize = field
        }

    override var subtitleText: String? = null
        set(value) {
            field = value
            subtitleTV.text = field
        }

    override var subtitleTextSize: Float =
        DEFAULT_SUBTITLE_TITLE_TEXT_SIZE
        set(value) {
            field = value
            subtitleTV.textSize = field
        }

    override var dimDrawable: Drawable? = drw(context, DEFAULT_DIM_ID)
        set(value) {
            field = value ?: field
            dimView.background = field
        }

    override var bottomGlowDrawable: Drawable? = drw(context, DEFAULT_BOTTOM_GLOW_ID)
        set(value) {
            field = value ?: field
            bottomGlowView.background = field
        }

    override var photoFrameColor: Int = col(context, DEFAULT_FRAME_COLOR_ID)
        set(value) {
            field = value
            photoFrameBackground.drawable.setColor(value)
        }

    override var photoFrameDrawable: Drawable = drw(context, DEFAULT_FRAME_DRAWABLE_ID)
        set(value) {
            field = value
            photoFrameBackground.apply {
                setImageDrawable(field)
                drawable.setColor(photoFrameColor)
            }
        }

    override fun setupWithViewPager(viewPager: ViewPager) {
        tabsEnabled = true
        tabs.setupWithViewPager(viewPager)
    }

    private val children: ArrayList<View> = arrayListOf()
    override val wallpaperImage: ImageView = ImageView(context).apply {
        id = R.id.wallpaper
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageDrawable(wallpaperDrawable)
        addTo(children)
    }
    override val dimView: View = View(context).apply {
        id = R.id.dim
        background = dimDrawable
        addTo(children)
    }
    override val bottomGlowView: View = View(context).apply {
        id = R.id.bottom_glow
        background = bottomGlowDrawable
        addTo(children)
    }
    override val photoImage: SquareImageView = SquareImageView(context).apply {
        id = R.id.photo
        adjustViewBounds = true
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            val margin = dpToPx(context, DEFAULT_FRAME_THICKNESS_DP)
            setMargins(margin, margin, margin, margin)
        }
        setImageDrawable(photoDrawable)
    }
    override val photoFrameBackground: SquareImageView = SquareImageView(context).apply {
        id = R.id.photo
        adjustViewBounds = true
        setImageDrawable(photoFrameDrawable)
    }

    override val photoFrame: FrameLayout = FrameLayout(context).apply {
        id = R.id.photo_frame
        addView(photoFrameBackground)
        addView(photoImage)
        addTo(children)
    }
    override val titleTV: TextView = TextView(context).apply {
        id = R.id.title
        text = titleText
        textSize = titleTextSize
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        setTextColor(fontColor)
        addTo(children)
    }
    override val subtitleTV: TextView = TextView(context).apply {
        id = R.id.subtitle
        text = subtitleText
        textSize = subtitleTextSize
        setTextColor(fontColor)
        addTo(children)
    }

    override val tabs: ProfileTabLayout = ProfileTabLayout(context).apply {
        enabled = tabsEnabled
        id = R.id.tabs
        setSelectedTabIndicatorColor(tabsIndicatorColor)
        setTabTextColors(tabsUnselectedColor, tabsSelectedColor)
        addTo(children)
    }

    override val optionButton: ImageButton = ImageButton(context).apply {
        id = R.id.option_button
        background = drw(context, R.color.colorTransparent)
        rotation = 90f
        setImageDrawable(drw(context, R.drawable.profile_ic_more_vert))
        setOnClickListener {
            popupWindow.showAsDropDown(popupAnchor)
        }
        addTo(children)
    }

    private val popupAnchor = View(context).apply {
        id = R.id.popup_anchor
        layoutParams = LayoutParams(0, 0).apply {
            val margin = dpToPx(context, 12)
            setMargins(margin, margin, margin, margin)
        }
        addTo(children)
    }

    override val popupWindow = ProfileOptionWindow(
        LayoutInflater.from(context).inflate(R.layout.profile_popup_window, this, false),
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = dpToPx(context, 12).toFloat()
            isOutsideTouchable = true
        }
    }

    init {
        addChildren(children)
    }

    private var translationName: AnimationHelper? = null
    private var translationJoinedOn: AnimationHelper? = null
    private var translationFrame: AnimationHelper? = null
    private var alphaPhoto: AnimationHelper? = null
    private var scalePhoto: AnimationHelper? = null
    private var rotationOptionButton: AnimationHelper? = null
    private var alphaDim: AnimationHelper? = null

    private var animators = ArrayList<AnimationHelper>()

    private val fullSizeConstraintSet = ConstraintSet()
    private val collapsedConstraintSet = ConstraintSet()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (parent is AppBarLayout) {
            appBar = parent as AppBarLayout
            appBar.addOnOffsetChangedListener(this)

            fullSizeConstraintSet.clone(context, R.layout.toolbar_profile)
            collapsedConstraintSet.clone(context, R.layout.toolbar_profile_collapsed)

            fullSizeConstraintSet.applyTo(this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (constraintsChanged) {
            animators.cancelAll()
            animators.evaluateAll()

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

    private fun initAnimators() {
        titleTV.let {
            translationName ?: run {
                translationName = HorizontalAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION
                ).addTo(animators)
            }
        }
        subtitleTV.let {
            translationJoinedOn ?: run {
                translationJoinedOn = HorizontalAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION
                ).addTo(animators)
            }
        }
        photoFrame.let {
            translationFrame ?: run {
                translationFrame = PlainTranslationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_SHORTER
                ).addTo(animators)
            }
            alphaPhoto ?: run {
                alphaPhoto = ReturnAlphaAnimationHelper(
                    it,
                    DecelerateAccelerateInterpolator(),
                    DURATION_MEDIUM
                ).addTo(animators)
            }
            scalePhoto ?: run {
                scalePhoto =
                    ReturnScaleAnimationHelper(
                        0.4f,
                        it,
                        DecelerateAccelerateInterpolator(),
                        DURATION_MEDIUM
                    ).addTo(animators)
            }
        }
        optionButton.let {
            rotationOptionButton ?: run {
                rotationOptionButton = RotationAnimationHelper(
                    it,
                    OvershootInterpolator(),
                    DURATION_SHORT
                ).addTo(animators)
            }
        }
        dimView.let {
            alphaDim ?: run {
                alphaDim = AlphaAnimationHelper(
                    it,
                    DecelerateInterpolator(),
                    DURATION_SHORT
                ).addTo(animators)
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
                    initAnimators()

                collapsedConstraintSet.applyTo(this)
                constraintsChanged = true
                dimAdjusted = false
                toolbarOpen = false

            } else if (!toolbarOpen && progress < TRANSITION_THRESHOLD) {
                fullSizeConstraintSet.applyTo(this)
                constraintsChanged = true
                dimAdjusted = false
                toolbarOpen = true
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animators.cancelAll()
    }
}