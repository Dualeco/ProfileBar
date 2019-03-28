package com.dichotome.profilebarapp.ui.mainBinding

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dichotome.profilebarapp.R
import com.dichotome.profilebarapp.util.constant.drw
import com.dichotome.profilebar.stubs.fragments.FavouritesTabFragment
import com.dichotome.profilebarbinding.stubs.fragments.SubscriptionsTabBindingFragment

class ProfileBindingLogic : ViewModel() {
    var photo: Drawable? = null
    var wallpaper = MutableLiveData<Drawable>()
    val title = "Pablo Picasso"
    val subtitle = "Joined on 5 December 1960"
    val pagerFragments = arrayListOf(
        SubscriptionsTabBindingFragment.newInstance("Subscriptions"),
        FavouritesTabFragment.newInstance("Favourites")
    )
    val isEditable: Boolean = true
    val isOwn = false
    val isFollowed = true

    fun onUsernameChanged() {
        wallpaper.value = drw(R.drawable.picasso_wall)
    }
}