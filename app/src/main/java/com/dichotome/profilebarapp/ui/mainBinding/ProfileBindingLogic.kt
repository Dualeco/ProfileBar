package com.dichotome.profilebarapp.ui.mainBinding

import androidx.lifecycle.ViewModel
import com.dichotome.profilebarapp.R
import com.dichotome.profilebarapp.util.constant.drw
import com.dichotome.profilebar.stubs.fragments.FavouritesTabFragment
import com.dichotome.profilebarbinding.stubs.fragments.SubscriptionsTabBindingFragment

class ProfileBindingLogic : ViewModel() {
    val photo = drw(R.drawable.avatar)
    val wallpaper = drw(R.drawable.cover2)
    val title = "Pavlo Bohdan"
    val subtitle = "Joined on 19 Apr 2017"
    val pagerFragments = arrayListOf(
        SubscriptionsTabBindingFragment.newInstance("Subsriptions (324)"),
        FavouritesTabFragment.newInstance("Favourites (626)")
    )

    fun onChangedPhoto() {}
    val onChangedWallpaper: () -> Unit = {}
}