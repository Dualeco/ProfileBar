package com.dichotome.profilebar.ui.profileBar.toolbar

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import com.dichotome.profilebar.R
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.ui.ProfileTabLayout
import com.dichotome.profilebar.ui.profileBar.ProfileBarViews
import com.dichotome.profilebar.util.extensions.addViews
import com.dichotome.profilephoto.ui.ZoomingImageView
import com.dichotome.profileshared.extensions.dpToPx
import com.dichotome.profileshared.extensions.drw
import com.dichotome.profileshared.extensions.hideKeyboard
import com.dichotome.profileshared.views.CircularImageView

abstract class ProfileToolbarBare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), ProfileBarViews {

    private var imageId = 0

    final override val wallpaperImage: ImageView = ImageView(context).apply {
        id = R.id.wallpaper
        scaleType = ImageView.ScaleType.CENTER
    }

    final override val dimView: View = View(context).apply { id = R.id.dim }

    final override val bottomGlowView: View = View(context).apply { id = R.id.bottom_glow }

    final override val photoImage: ZoomingImageView = ZoomingImageView(context).apply {
        id = R.id.photo
        adjustViewBounds = true
        isCircular = true
    }

    final override val photoFrameBackground: CircularImageView = CircularImageView(context).apply {
        id = R.id.photo_frame_background
        adjustViewBounds = true
    }

    final override val photoFrame: FrameLayout = FrameLayout(context).apply {
        id = R.id.photo_frame
        addView(photoFrameBackground)
        addView(photoImage)
    }

    final override val titleTV: TextView = TextView(context).apply {
        id = R.id.title
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }

    protected var onEditCanceledListener: (() -> Unit)? = null
    final override val editTitle: EditText = object : EditText(context) {
        override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hideKeyboard()
                onEditCanceledListener?.invoke()
            }
            return true
        }
    }.apply {
        imeOptions = EditorInfo.IME_ACTION_GO
        gravity = Gravity.CENTER
        id = R.id.editTitle
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        maxLines = 1
    }

    final override val subtitleTV: TextView = TextView(context).apply { id = R.id.subtitle }

    final override val tabs: ProfileTabLayout = ProfileTabLayout(context).apply { id = R.id.tabs }

    final override val optionButton: ImageButton = ImageButton(context).apply {
        id = R.id.option_button
        setImageDrawable(drw(R.drawable.profile_ic_more_vert))
        background = drw(R.color.colorTransparent)
        rotation = 90f
        setOnClickListener {
            optionWindow.showAsDropDown(popupAnchor)
        }
    }

    final override val followButton: ImageButton = ImageButton(context).apply {
        id = R.id.follow_button
        setImageDrawable(drw(R.drawable.ic_notifications_none))
        background = drw(R.color.colorTransparent)
    }

    private val popupAnchor = View(context).apply {
        id = R.id.popup_anchor
        layoutParams = LayoutParams(0, 0).apply {
            setMargins(dpToPx(12))
        }
    }

    final override val optionWindow = ProfileOptionWindow(
        LayoutInflater.from(context).inflate(R.layout.profile_popup_window, this, false),
        WRAP_CONTENT,
        WRAP_CONTENT
    ).apply {
        elevation = dpToPx(8).toFloat()
        isOutsideTouchable = true
    }

    init {
        addViews(
            wallpaperImage,
            dimView,
            bottomGlowView,
            photoFrame,
            titleTV,
            editTitle,
            subtitleTV,
            tabs,
            optionButton,
            followButton,
            popupAnchor
        )
    }
}