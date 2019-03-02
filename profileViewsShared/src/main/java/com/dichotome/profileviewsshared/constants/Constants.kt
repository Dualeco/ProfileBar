package com.dichotome.profileviewsshared.constants

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.dichotome.profileviewsshared.extensions.getNavBarHeight
import com.dichotome.profileviewsshared.extensions.getStatusBarHeight
import com.dichotome.profileviewsshared.extensions.getToolbarHeight

class Constants(val context: Context) {
    val DISPLAY_WIDTH: Int = Resources.getSystem().displayMetrics.widthPixels
    val DISPLAY_HEIGHT: Int = Resources.getSystem().displayMetrics.heightPixels
    val STATUS_BAR_SIZE: Int
        get() = context.getStatusBarHeight()
    val TOOLBAR_SIZE: Int
        get() = context.getToolbarHeight()
    val NAVBAR_SIZE: Int
        get() = context.getNavBarHeight()
    val CONTAINER_TOP_MARGIN = -STATUS_BAR_SIZE

    fun android(api: Int, action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= api) action()
    }

    fun androidIf(api: Int, action: () -> Unit, action2: () -> Unit) {
        if (Build.VERSION.SDK_INT >= api) action() else action2()
    }

    fun <T> androidResult(api: Int, action: () -> T?): T? = if (Build.VERSION.SDK_INT >= api) action() else null
}