package com.dichotome.profilebar.ui.profileBar.toolbar

interface ProfileBarActions {
    fun setOnChangePhoto(action: () -> Unit)
    fun setOnChangeWallpaper(action: () -> Unit)
    fun setOnChangeUsername(action: () -> Unit)
    fun setOnLogOut(action: () -> Unit)
    fun setOnUsernameChangeFinished(action: (String) -> Unit)
    fun setOnFollowed(action: () -> Unit)
    fun setOnUsernameChangeCanceled(action: () -> Unit)
}