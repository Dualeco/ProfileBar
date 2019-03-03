package com.dichotome.profilebar.ui.profileBar

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.dichotome.profilebar.ui.ProfileOptionWindow
import com.dichotome.profilebar.ui.ProfileTabLayout
import com.dichotome.profileshared.views.SquareRoundedImageView

interface ProfileBarResources {
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