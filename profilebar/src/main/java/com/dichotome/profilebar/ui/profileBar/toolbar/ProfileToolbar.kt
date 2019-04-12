package com.dichotome.profilebar.ui.profileBar.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
import androidx.core.view.setMargins
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.profileBar.ProfileBarResources
import com.dichotome.profilebar.util.extensions.download
import com.dichotome.profilebar.util.extensions.setColor
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.*

open class ProfileToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ProfileToolbarBare(context, attrs, defStyle), ProfileBarResources, ProfileBarActions {

    companion object {
        private val DEFAULT_TEXT_COLOR_ID = R.color.colorPrimary
        private val DEFAULT_HINT_COLOR_ID = R.color.colorPrimaryUnselected
        private const val DEFAULT_TITLE_TEXT_SIZE = 18f
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

    final override var isOwnProfile: Boolean = false
        set(value) {
            field = value
            optionButton.isDisplayed = value
            followButton.isDisplayed = !value
        }

    final override var isFollowed: Boolean = false
        set(value) {
            field = value
            followButton.setImageDrawable(
                if (value)
                    drw(R.drawable.ic_notifications_active)
                else
                    drw(R.drawable.ic_notifications_none)
            )
        }

    final override var isTitleEditable: Boolean = false
        set(value) {
            field = value
            editTitle.isDisplayed = value
            titleTV.visibility = if (value) View.INVISIBLE else View.VISIBLE

            if (field) editTitle.apply {
                editTitle.setText(
                    titleTV.text,
                    TextView.BufferType.EDITABLE
                )
                requestFocus()
                setSelection(text.length)
                showKeyboard()
            }
        }

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
            wallpaperImage.download(field)
        }

    private val photoImageOptions = RequestOptions().override(Constants(context).DISPLAY_WIDTH)
    override var photo: Drawable? = null
        set(value) {
            field = value
            photoImage.download(field, photoImageOptions)
        }

    override var fontColor: Int = col(DEFAULT_TEXT_COLOR_ID)
        set(value) {
            field = value
            titleTV.setTextColor(value)
            editTitle.setTextColor(value)
            subtitleTV.setTextColor(value)
        }

    override var hintTextColor = col(DEFAULT_HINT_COLOR_ID)
        set(value) {
            field = value
            editTitle.setHintTextColor(value)
        }

    override var title: String? = null
        set(value) {
            field = value
            titleTV.text = field
            editTitle.setText(field, TextView.BufferType.EDITABLE)
            editTitle.hint = field
        }

    override var titleSize: Float =
        DEFAULT_TITLE_TEXT_SIZE
        set(value) {
            field = value
            titleTV.textSize = field
            editTitle.textSize = field
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

    override fun setOnChangePhoto(action: () -> Unit) {
        optionWindow.changePhotoButton.setOnClickListener {
            action()
        }
    }

    override fun setOnChangeWallpaper(action: () -> Unit) {
        optionWindow.changeWallpaperButton.setOnClickListener {
            action()
        }
    }

    override fun setOnChangeUsername(action: () -> Unit) {
        optionWindow.changeUsernameButton.setOnClickListener {
            action()
        }
    }

    override fun setOnLogOut(action: () -> Unit) {
        optionWindow.logOutButton.setOnClickListener {
            action()
        }
    }

    final override fun setOnUsernameChangeFinished(action: (String) -> Unit) {
        editTitle.apply {
            setOnEditorActionListener { _, _, _ ->
                hideKeyboard()
                clearFocus()
                action(text.toString())
                true
            }
        }
    }

    final override fun setOnFollowed(action: () -> Unit) {
        followButton.setOnClickListener {
            action()
        }
    }

    final override fun setOnUsernameChangeCanceled(action: () -> Unit) {
        onEditCanceledListener = action
    }

    init {
        dimView.background = dimDrawable
        bottomGlowView.background = bottomGlowDrawable

        photoImage.apply {
            setImageResource(DEFAULT_PHOTO_ID)
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                setMargins(dpToPx(DEFAULT_FRAME_THICKNESS_DP))
            }
        }
        wallpaperImage.setImageResource(DEFAULT_WALLPAPER_ID)
        photoFrameBackground.setImageDrawable(photoFrameDrawable)

        titleTV.apply {
            text = title
            textSize = titleSize
            setTextColor(fontColor)
        }
        editTitle.apply {
            setText(title, TextView.BufferType.EDITABLE)
            textSize = titleSize
            setTextColor(fontColor)
            setHintTextColor(hintTextColor)
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
        isOwnProfile = true
        isFollowed = false
        isTitleEditable = false

        setOnFollowed {  }
        setOnUsernameChangeFinished {  }
    }
}
