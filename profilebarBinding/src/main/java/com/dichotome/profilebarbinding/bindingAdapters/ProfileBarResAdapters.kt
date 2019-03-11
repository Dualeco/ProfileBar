package com.dichotome.profilebarbinding.bindingAdapters

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.ui.profileBar.ProfileBar
import com.dichotome.profileshared.extensions.col
import com.dichotome.profileshared.extensions.drw
import com.dichotome.profileshared.extensions.str


@BindingAdapter("app:photo")
fun setPhoto(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.photo = bar.drw(id)
    }
}

@BindingAdapter("app:wallpaper")
fun setWallpaper(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.wallpaper = bar.drw(id)
    }
}

@BindingAdapter("app:fontColor")
fun setFontColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.fontColor = bar.col(id)
    }
}


@BindingAdapter("app:title")
fun setTitle(bar: ProfileBar, @StringRes id: Int?) {
    id?.let {
        bar.title = bar.str(id)
    }
}

@BindingAdapter("app:subtitle")
fun setSubtitle(bar: ProfileBar, @StringRes id: Int?) {
    id?.let {
        bar.subtitle = bar.str(id)
    }
}

@BindingAdapter("app:tabSelectedColor")
fun setTabSelectedColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsSelectedColor = bar.col(id)
    }
}

@BindingAdapter("app:frameColor")
fun setFrameColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.photoFrameColor = bar.col(id)
    }
}

@BindingAdapter("app:frameDrawable")
fun setFrameDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.dimDrawable = bar.drw(id)
    }
}

@BindingAdapter("app:tabUnselectedColor")
fun setTabUnselectedColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsUnselectedColor = bar.col(id)
    }
}

@BindingAdapter("app:tabIndicatorColor")
fun setTabIndicatorColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.tabsIndicatorColor = bar.col(id)
    }
}

@BindingAdapter("app:dimDrawable")
fun setDimDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.dimDrawable = bar.drw(id)
    }
}

@BindingAdapter("app:bottomGlowDrawable")
fun setBottomGlowDrawable(bar: ProfileBar, @DrawableRes id: Int?) {
    id?.let {
        bar.bottomGlowDrawable = bar.drw(id)
    }
}
