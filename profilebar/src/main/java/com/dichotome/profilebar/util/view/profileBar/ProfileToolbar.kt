package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.util.constant.Constants
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilebar.util.constant.dpToPx
import com.dichotome.profilebar.util.constant.drw
import com.dichotome.profilebar.util.view.extensions.addChildren
import com.dichotome.profilebar.util.view.extensions.addTo
import com.dichotome.profilebar.util.view.extensions.download
import com.dichotome.profilebar.util.view.extensions.setColor
import com.dichotome.profilephoto.view.ZoomableRoundImageView
import com.dichotome.profileviewsshared.views.SquareRoundedImageView

open class ProfileToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), ProfileBarInterface {

    companion object {
        const val TAG = "Toolbar"

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
    protected var profilePager: ViewPager? = null

    var profilePicOptions = RequestOptions()
        .override(Constants(context).DISPLAY_WIDTH)
        .centerCrop()

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
            photoImage.download(field, profilePicOptions)
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
        profilePager = viewPager
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

    override val photoImage: ZoomableRoundImageView = ZoomableRoundImageView(context).apply {
        id = R.id.photo
        adjustViewBounds = true
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            val margin = dpToPx(context, DEFAULT_FRAME_THICKNESS_DP)
            setMargins(margin, margin, margin, margin)
        }
        download(R.drawable.cover2, profilePicOptions)
    }

    override val photoFrameBackground: SquareRoundedImageView = SquareRoundedImageView(context).apply {
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
        elevation = dpToPx(context, 12).toFloat()
        isOutsideTouchable = true
    }

    init {
        addChildren(children)
    }
}
