package com.dichotome.profilebarbinding.bindingAdapters

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.ui.profileBar.ProfileBar
import com.dichotome.profilebar.util.extensions.download
import com.dichotome.profileshared.constants.Constants

@BindingAdapter("app:photo")
fun setPhoto(bar: ProfileBar, photo: Drawable?) {
    bar.photo = photo
}

@BindingAdapter("app:photoUri")
fun setPhotoUri(bar: ProfileBar, uri: Uri) {
    val profilePicOptions = RequestOptions()
        .override(Constants(bar.context).DISPLAY_WIDTH)
        .centerCrop()

    bar.photoImage.download(uri, profilePicOptions)
}

@BindingAdapter("app:wallpaper")
fun setWallpaper(bar: ProfileBar, wallpaper: Drawable?) {
    bar.wallpaper = wallpaper
}

@BindingAdapter("app:wallpaperUri")
fun setWallpaperUri(bar: ProfileBar, uri: Uri) {
    val wallpaperOptions = RequestOptions()
        .centerCrop()

    bar.wallpaperImage.download(uri, wallpaperOptions)
}

@BindingAdapter("app:title")
fun setTitle(bar: ProfileBar, title: String?) {
    bar.title = title
}

@BindingAdapter("app:titleSize")
fun setTitleSize(bar: ProfileBar, dp: Int?) {
    dp?.let {
        bar.titleSize = dp.toFloat()
    }
}

@BindingAdapter("app:subtitle")
fun setSubtitle(bar: ProfileBar, subtitle: String?) {
    bar.subtitle = subtitle
}


@BindingAdapter("app:subtitleSize")
fun setSubtitleSize(bar: ProfileBar, dp: Int?) {
    dp?.let {
        bar.subtitleSize = dp.toFloat()
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


@BindingAdapter("app:frameDrawable")
fun setFrameDrawable(bar: ProfileBar, frame: Drawable?) {
    frame?.let {
        bar.photoFrameDrawable = frame
    }
}

@BindingAdapter("app:onPhotoChanged")
fun setOnChangedPhoto(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.optionWindow.changePhotoButton.setOnClickListener {
            listener()
        }
    }
}

@BindingAdapter("app:onWallpaperChanged")
fun setOnChangedWallpaper(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.optionWindow.changeWallpaperButton.setOnClickListener {
            listener()
        }
    }
}

@BindingAdapter("app:onUsernameChanged")
fun setOnChangedUsername(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.optionWindow.changeUsernameButton.setOnClickListener {
            listener()
        }
    }
}


@BindingAdapter("app:onLoggedOut")
fun setOnLoggedOut(bar: ProfileBar, listener: (() -> Unit)?) {
    listener?.let {
        bar.optionWindow.logOutButton.setOnClickListener {
            listener()
        }
    }
}