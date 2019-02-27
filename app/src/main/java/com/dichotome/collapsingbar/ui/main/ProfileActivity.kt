package com.dichotome.collapsingbar.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dichotome.profilebar.ui.fragment.FavouritesTabFragment
import com.dichotome.profilebar.ui.fragment.SubscriptionsTabFragment
import com.dichotome.collapsingbar.R
import com.dichotome.collapsingbar.util.constant.drw
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_profile)

        val photo = drw(R.drawable.avatar)
        val wallpaper = drw(R.drawable.cover2)
        val pagerFragments = arrayListOf(
            SubscriptionsTabFragment.newInstance("Subsriptions (324)"),
            FavouritesTabFragment.newInstance("Favourites (626)")
        )

        profileBar.apply {
            photoDrawable = photo
            subtitleText = "Joined on 19 April 2017"
            titleText = "Pavlo Bondan"
            wallpaperDrawable = wallpaper
            tabsEnabled = true
        }
        profilePager.fragments = pagerFragments
        profileBar.setupWithViewPager(profilePager)

        profileBar.popupWindow.changePhotoButton.setOnClickListener {

        }
        profileBar.popupWindow.changeWallpaperButton.setOnClickListener {

        }
    }
}