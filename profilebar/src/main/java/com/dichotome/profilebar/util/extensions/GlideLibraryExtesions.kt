package com.dichotome.profilebar.util.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profileshared.extensions.drw


fun ImageView.download(@DrawableRes res: Int, options: RequestOptions? = null) {
    glideDownload(drw(res), options)
}

fun ImageView.download(url: String, options: RequestOptions? = null) {
    glideDownload(url, options)
}

fun ImageView.download(uri: Uri, options: RequestOptions? = null) {
    glideDownload(uri, options)
}

fun ImageView.download(drw: Drawable?, options: RequestOptions? = null) {
    glideDownload(drw, options)
}

fun ImageView.download(drw: Drawable?, circular: Boolean) {
    glideDownload(drw, RequestOptions().apply {
        if (circular)
            circleCrop()
    })
}

private fun ImageView.glideDownload(obj: Any?, options: RequestOptions?) {
    val requestOptions = RequestOptions()

    options?.let {
        requestOptions.apply {
            apply(it)
            format(DecodeFormat.PREFER_RGB_565)
        }

    }

    Glide.with(context)
        .load(obj)
        .apply(requestOptions)
        .into(this)
}