package com.dichotome.profilebar.ui.profileBar.toolbar

import android.content.Context
import android.graphics.Typeface
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import com.dichotome.profileshared.views.SquareRoundedImageView

abstract class ProfileToolbarBare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), ProfileBarViews {

    private var imageId = 0

    final override val wallpaperImage: ImageView = ImageView(context).apply {
        id = R.id.wallpaper
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    final override val dimView: View = View(context).apply { id = R.id.dim }

    final override val bottomGlowView: View = View(context).apply { id = R.id.bottom_glow }

    final override val photoImage: ZoomingImageView = ZoomingImageView(context).apply {
        id = R.id.photo
        adjustViewBounds = true
        makeCircular()
    }

    final override val photoFrameBackground: SquareRoundedImageView = SquareRoundedImageView(context).apply {
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

    final override val subtitleTV: TextView = TextView(context).apply { id = R.id.subtitle }

    final override val tabs: ProfileTabLayout = ProfileTabLayout(context).apply { id = R.id.tabs }

    final override val optionButton: ImageButton = ImageButton(context).apply {
        id = R.id.option_button
        setImageDrawable(drw(R.drawable.profile_ic_more_vert))
        background = drw(R.color.colorTransparent)
        rotation = 90f
        setOnClickListener {
            popupWindow.showAsDropDown(popupAnchor)
        }
    }

    private val popupAnchor = View(context).apply {
        id = R.id.popup_anchor
        layoutParams = LayoutParams(0, 0).apply {
            setMargins(dpToPx(12))
        }
    }

    final override val popupWindow = ProfileOptionWindow(
        LayoutInflater.from(context).inflate(R.layout.profile_popup_window, this, false),
        WRAP_CONTENT,
        WRAP_CONTENT
    ).apply {
        elevation = dpToPx(12).toFloat()
        isOutsideTouchable = true
    }

    init {
        addViews(
            wallpaperImage,
            dimView,
            bottomGlowView,
            photoFrame,
            titleTV,
            subtitleTV,
            tabs,
            optionButton,
            popupAnchor
        )
    }
}