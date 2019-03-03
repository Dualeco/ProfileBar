package com.dichotome.profilebarapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dichotome.profilebarapp.R
import com.dichotome.profilebarapp.util.constant.drw
import com.dichotome.profilebar.stubFragments.SubscriptionsTabFragment
import com.dichotome.profilebar.stubFragments.FavouritesTabFragment
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