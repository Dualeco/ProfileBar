package com.dichotome.profilebarBinding

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilebar.util.constant.drw
import com.dichotome.profilebar.util.constant.str
import com.dichotome.profilebar.util.view.profileBar.ProfileBar


@BindingAdapter("app:photo")
fun setPhoto(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.photoDrawable = drw(bar.context, id)
    }
}

@BindingAdapter("app:wallpaper")
fun setWallpaper(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.wallpaperDrawable = drw(bar.context, id)
    }
}

@BindingAdapter("app:fontColor")
fun setFontColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.fontColor = col(bar.context, id)
    }
}


@BindingAdapter("app:title")
fun setTitle(bar: ProfileBar, @StringRes id: Int?) {
    id?.let {
        bar.titleText = str(bar.context, id)
    }
}

@BindingAdapter("app:subtitle")
fun setSubtitle(bar: ProfileBar, @StringRes id: Int?) {
    id?.let {
        bar.subtitleText = str(bar.context, id)
    }
}

@BindingAdapter("app:tabSelectedColor")
fun setTabSelectedColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsSelectedColor = col(bar.context, id)
    }
}

@BindingAdapter("app:frameDrawable")
fun setFrameDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.dimDrawable = drw(bar.context, id)
    }
}

@BindingAdapter("app:tabUnselectedColor")
fun setTabUnselectedColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsUnselectedColor = col(bar.context, id)
    }
}

@BindingAdapter("app:tabIndicatorColor")
fun setTabIndicatorColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsIndicatorColor = col(bar.context, id)
    }
}

@BindingAdapter("app:dimDrawable")
fun setDimDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.dimDrawable = drw(bar.context, id)
    }
}

@BindingAdapter("app:bottomGlowDrawable")
fun setBottomGlowDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.bottomGlowDrawable = drw(bar.context, id)
    }
}
