package com.dichotome.profilebarapp.ui.mainBinding

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.EditText
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
    val isOwn = true
    val isFollowed = true

    fun onUsernameChangeFinished(text: String) {

    }
}