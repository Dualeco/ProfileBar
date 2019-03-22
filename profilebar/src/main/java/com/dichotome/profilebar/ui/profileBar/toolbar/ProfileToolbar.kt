package com.dichotome.profilebar.ui.profileBar.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
import androidx.core.view.setMargins
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.profileBar.ProfileBarResources
import com.dichotome.profilebar.util.extensions.download
import com.dichotome.profilebar.util.extensions.setColor
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.col
import com.dichotome.profileshared.extensions.dpToPx
import com.dichotome.profileshared.extensions.drw

open class ProfileToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ProfileToolbarBare(context, attrs, defStyle), ProfileBarResources {

    companion object {
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
        private val DEFAULT_PHOTO_ID = R.drawable.default_avatar
        private val DEFAULT_WALLPAPER_ID = R.drawable.default_wallpaper
    }

    private val photoImageOptions = RequestOptions()
        .override(Constants(context).DISPLAY_WIDTH)
        .centerCrop()

    private val wallpaperImageOptions = RequestOptions()
        .centerCrop()

    override var tabsEnabled: Boolean = false
        set(value) {
            field = value
            tabs.enabled = field
        }

    override var tabsSelectedColor: Int = col(DEFAULT_TABS_SELECTED_COLOR_ID)
        set(value) {
            field = value
            tabs.setTabTextColors(tabsUnselectedColor, field)
        }

    override var tabsUnselectedColor: Int = col(DEFAULT_TABS_UNSELECTED_COLOR_ID)
        set(value) {
            field = value
            tabs.setTabTextColors(field, tabsSelectedColor)
        }

    override var tabsIndicatorColor: Int = col(DEFAULT_TABS_SELECTED_COLOR_ID)
        set (value) {
            field = value
            tabs.setSelectedTabIndicatorColor(field)
        }

    final override var wallpaper: Drawable? = null
        set(value) {
            field = value
            wallpaperImage.download(field, wallpaperImageOptions)
        }

    override var photo: Drawable? = null
        set(value) {
            field = value
            photoImage.download(field, photoImageOptions)
        }

    override var fontColor: Int = col(DEFAULT_TEXT_COLOR_ID)
        set(value) {
            field = value
            titleTV.setTextColor(value)
            subtitleTV.setTextColor(value)
        }

    override var title: String? = null
        set(value) {
            field = value
            titleTV.text = field
        }

    override var titleSize: Float =
        DEFAULT_TITLE_TEXT_SIZE
        set(value) {
            field = value
            titleTV.textSize = field
        }

    override var subtitle: String? = null
        set(value) {
            field = value
            subtitleTV.text = field
        }

    override var subtitleSize: Float =
        DEFAULT_SUBTITLE_TITLE_TEXT_SIZE
        set(value) {
            field = value
            subtitleTV.textSize = field
        }

    final override var dimDrawable: Drawable? = drw(DEFAULT_DIM_ID)
        set(value) {
            field = value ?: field
            dimView.background = field
        }

    final override var bottomGlowDrawable: Drawable? = drw(DEFAULT_BOTTOM_GLOW_ID)
        set(value) {
            field = value ?: field
            bottomGlowView.background = field
        }

    override var photoFrameColor: Int = col(DEFAULT_FRAME_COLOR_ID)
        set(value) {
            field = value
            photoFrameBackground.drawable.setColor(value)
        }

    final override var photoFrameDrawable: Drawable = drw(DEFAULT_FRAME_DRAWABLE_ID)
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

    init {
        photoImage.download(DEFAULT_PHOTO_ID, photoImageOptions)
        wallpaperImage.download(DEFAULT_WALLPAPER_ID, wallpaperImageOptions)

        dimView.background = dimDrawable
        bottomGlowView.background = bottomGlowDrawable

        photoImage.apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                setMargins(dpToPx(DEFAULT_FRAME_THICKNESS_DP))
            }
        }
        photoFrameBackground.setImageDrawable(photoFrameDrawable)

        titleTV.apply {
            text = title
            textSize = titleSize
            setTextColor(fontColor)
        }
        subtitleTV.apply {
            text = subtitle
            textSize = subtitleSize
            setTextColor(fontColor)
        }

        tabs.apply {
            enabled = tabsEnabled
            setSelectedTabIndicatorColor(tabsIndicatorColor)
            setTabTextColors(tabsUnselectedColor, tabsSelectedColor)
        }
    }
}
