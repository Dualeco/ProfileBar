package com.dichotome.profilebarbinding

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.util.constant.col
import com.dichotome.profilebar.util.view.profileBar.ProfileBar

@BindingAdapter("app:photo")
fun setPhoto(bar: ProfileBar, photo: Drawable?) {
    bar.photoDrawable = photo
}

@BindingAdapter("app:wallpaper")
fun setWallpaper(bar: ProfileBar, wallpaper: Drawable?) {
    bar.wallpaperDrawable = wallpaper
}

@BindingAdapter("app:title")
fun setTitle(bar: ProfileBar, title: String?) {
    bar.titleText = title
}

@BindingAdapter("app:titleSize")
fun setTitleSize(bar: ProfileBar, dp: Int?) {
    dp?.let {
        bar.titleTextSize = dp.toFloat()
    }
}

@BindingAdapter("app:subtitle")
fun setSubtitle(bar: ProfileBar, subtitle: String?) {
    bar.subtitleText = subtitle
}


@BindingAdapter("app:subtitleSize")
fun setSubtitleSize(bar: ProfileBar, dp: Int?) {
    dp?.let {
        bar.subtitleTextSize = dp.toFloat()
    }
}

@BindingAdapter("app:tabsEnabled")
fun setEnableTabs(bar: ProfileBar, enable: Boolean?) {
    enable?.let {
        bar.tabsEnabled = enable
    }
}

@BindingAdapter("app:barScrollFlags")
fun setScrollFlags(bar: ProfileBar, flags: Int?) {
    flags?.let {
        bar.setScrollFlags(flags)
    }
}

@BindingAdapter("app:dimDrawable")
fun setDimDrawable(bar: ProfileBar, dim: Drawable?) {
    dim?.let {
        bar.dimDrawable = dim
    }
}

@BindingAdapter("app:bottomGlowDrawable")
fun setBottomGlowDrawable(bar: ProfileBar, glow: Drawable?) {
    glow?.let {
        bar.bottomGlowDrawable = glow
    }
}

@BindingAdapter("app:frameColor")
fun setFrameColor(bar: ProfileBar, @ColorRes id: Int?) {
    id?.let {
        bar.photoFrameColor = col(bar.context, id)
    }
}

@BindingAdapter("app:frameDrawable")
fun setFrameDrawable(bar: ProfileBar, frame: Drawable?) {
    frame?.let {
        bar.photoFrameDrawable = frame
    }
}

@BindingAdapter("app:onChangedPhoto")
fun setOnChangedPhoto(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.popupWindow.changePhotoButton.setOnClickListener {
            listener()
        }
    }
}

@BindingAdapter("app:onChangedWallpaper")
fun setOnChangedWallpaper(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.popupWindow.changePhotoButton.setOnClickListener {
            listener()
        }
    }
}