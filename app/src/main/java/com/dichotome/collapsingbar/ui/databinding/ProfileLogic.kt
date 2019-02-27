package com.dichotome.collapsingbar.ui.databinding

import com.dichotome.profilebar.ui.fragment.FavouritesTabFragment
import com.dichotome.profilebar.ui.fragment.SubscriptionsTabFragment
import com.dichotome.collapsingbar.R
import com.dichotome.collapsingbar.ui.core.CoreLogic
import com.dichotome.collapsingbar.util.constant.drw

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