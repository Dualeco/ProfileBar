package com.dichotome.profilebar.ui.profileBar

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.ui.ProfileTabLayout
import com.dichotome.profileshared.views.CircularImageView

interface ProfileBarViews {

    val wallpaperImage: ImageView
    val dimView: View
    val bottomGlowView: View
    val photoImage: CircularImageView
    val photoFrameBackground: CircularImageView
    val photoFrame: FrameLayout
    val titleTV: TextView
    val subtitleTV: TextView
    val optionButton: ImageButton
    val tabs: ProfileTabLayout
    val optionWindow: ProfileOptionWindow

}