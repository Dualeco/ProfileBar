package com.dichotome.profilebar.ui.profileBar.toolbar

import android.widget.EditText

interface ProfileBarActions {
    fun setOnChangePhoto(action: () -> Unit)
    fun setOnChangeWallpaper(action: () -> Unit)
    fun setOnChangeUsername(action: () -> Unit)
    fun setOnLogOut(action: () -> Unit)
    fun setOnUsernameChangeFinished(action: (EditText) -> Unit)
    fun setOnFollowed(action: () -> Unit)
}