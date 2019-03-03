package com.dichotome.profilebarapp.ui.databinding

import com.dichotome.profilebarapp.R
import com.dichotome.profilebarapp.ui.core.CoreLogic
import com.dichotome.profilebarapp.util.constant.drw
import com.dichotome.profilebar.stubFragments.FavouritesTabFragment
import com.dichotome.profilebarbinding.stubFragments.SubscriptionsTabFragment

class ProfileLogic : CoreLogic() {
    val photo = drw(R.drawable.avatar)
    val wallpaper = drw(R.drawable.cover2)
    val title = "Pavlo Bohdan"
    val subtitle = "Joined on 19 Apr 2017"
    val pagerFragments = arrayListOf(
        SubscriptionsTabFragment.newInstance("Subsriptions (324)"),
        FavouritesTabFragment.newInstance("Favourites (626)")
    )

    fun onChangedPhoto() {}
    val onChangedWallpaper: () -> Unit = {}
}