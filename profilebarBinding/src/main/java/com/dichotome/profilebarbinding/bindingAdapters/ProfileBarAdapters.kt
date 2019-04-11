package com.dichotome.profilebarbinding.bindingAdapters

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.arch.core.util.Function
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.ui.profileBar.ProfileBar
import com.dichotome.profilebar.util.extensions.download
import com.dichotome.profileshared.constants.Constants

@BindingAdapter("app:isOwnProfile")
fun setIsOwnProfile(bar: ProfileBar, isOwn: Boolean?) {
    isOwn?.let {
        bar.isOwnProfile = it
    }
}

@BindingAdapter("app:isFollowed")
fun setIsFollowed(bar: ProfileBar, isFollowed: Boolean?) {
    isFollowed?.let {
        bar.isFollowed = it
    }
}

@BindingAdapter("app:isTitleEditable")
fun setIsTitleEditable(bar: ProfileBar, isEditable: Boolean?) {
    isEditable?.let {
        bar.isTitleEditable = it
    }
}

@BindingAdapter("app:photo")
fun setPhoto(bar: ProfileBar, photo: Drawable?) {
    photo?.let {
        bar.photo = it
    }
}

@BindingAdapter("app:photoUri")
fun setPhotoUri(bar: ProfileBar, uri: Uri?) {
    uri?.let {

        bar.photoImage.download(uri, RequestOptions().override(Constants(bar.context).DISPLAY_WIDTH))
    }
}

@BindingAdapter("app:wallpaper")
fun setWallpaper(bar: ProfileBar, wallpaper: Drawable?) {
    wallpaper?.let {
        bar.wallpaper = it
    }
}

@BindingAdapter("app:wallpaperUri")
fun setWallpaperUri(bar: ProfileBar, uri: Uri?) {
    uri?.let {
        bar.wallpaperImage.download(uri)
    }
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

@BindingAdapter("app:onPhotoChange")
fun setOnChangePhoto(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnChangePhoto {
            it()
        }
    }
}

@BindingAdapter("app:onWallpaperChange")
fun setOnChangeWallpaper(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnChangeWallpaper {
            it()
        }
    }
}

@BindingAdapter("app:onUsernameChange")
fun setOnChangeUsername(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnChangeUsername {
            it()
        }
    }
}


@BindingAdapter("app:onLogOut")
fun setOnLogOut(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnLogOut {
            it()
        }
    }
}

@BindingAdapter("app:onUsernameChangeFinished")
fun setUsernameChangeFinished(bar: ProfileBar, action: Function<String, Unit>?) {
    action?.let {
        bar.setOnUsernameChangeFinished { text ->
            it.apply(text)
        }
    }
}

@BindingAdapter("app:onFollowed")
fun setOnFollowed(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnFollowed {
            it()
        }
    }
}

@BindingAdapter("app:onUsernameChangeCanceled")
fun setOnUsernameChangeCanceled(bar: ProfileBar, action: (() -> Unit)?) {
    action?.let {
        bar.setOnUsernameChangeCanceled {
            it()
        }
    }
}

