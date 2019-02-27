package com.dichotome.profilebar.util.view.profileBar

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.util.view.SquareImageView

interface ProfileBarInterface {
    // Views
    val wallpaperImage: ImageView
    val dimView: View
    val bottomGlowView: View
    val photoImage: SquareImageView
    val photoFrameBackground: SquareImageView
    val photoFrame: FrameLayout
    val titleTV: TextView
    val subtitleTV: TextView
    val optionButton: ImageButton
    val tabs: ProfileTabLayout
    val popupWindow: ProfileOptionWindow

    // View Properties
    var tabsEnabled: Boolean

    var tabsSelectedColor: Int
    var tabsUnselectedColor: Int
    var tabsIndicatorColor: Int

    var wallpaperDrawable: Drawable?
    var photoDrawable: Drawable?

    var fontColor: Int
    var titleText: String?
    var titleTextSize: Float
    var subtitleText: String?
    var subtitleTextSize: Float

    var dimDrawable: Drawable?
    var bottomGlowDrawable: Drawable?

    var photoFrameColor: Int
    var photoFrameDrawable: Drawable

    fun setupWithViewPager(viewPager: ViewPager)
}