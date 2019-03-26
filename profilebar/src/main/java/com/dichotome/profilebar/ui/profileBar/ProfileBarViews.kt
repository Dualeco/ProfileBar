package com.dichotome.profilebar.ui.profileBar

import android.view.View
import android.widget.*
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
    val editTitle: EditText
    val subtitleTV: TextView
    val optionButton: ImageButton
    val followButton: ImageButton
    val tabs: ProfileTabLayout
    val optionWindow: ProfileOptionWindow
}