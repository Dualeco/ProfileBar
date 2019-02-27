package com.dichotome.profilebar.ui

import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.dichotome.profilebar.R

class ProfileOptionWindow(contentView: View, width: Int, height: Int) : PopupWindow(
    contentView, width, height
) {
    val changePhotoButton: LinearLayout = contentView.findViewById(R.id.change_photo)
    val changeWallpaperButton: LinearLayout = contentView.findViewById(R.id.change_wallpaper)
}