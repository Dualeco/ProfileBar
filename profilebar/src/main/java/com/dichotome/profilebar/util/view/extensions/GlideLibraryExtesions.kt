package com.dichotome.profilebar.util.view.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.dichotome.profilebar.R
import com.dichotome.profilebar.util.constant.drw


fun ImageView.download(@DrawableRes res: Int, options: RequestOptions? = null) {
    glideDownload(drw(context, res), options)
}

fun ImageView.download(url: String, options: RequestOptions? = null) {
    glideDownload(url, options)
}

fun ImageView.download(drw: Drawable?, options: RequestOptions? = null) {
    glideDownload(drw, options)
}

private fun ImageView.glideDownload(obj: Any?, options: RequestOptions?) {
    val requestOptions = RequestOptions()

    options?.let {
        requestOptions.apply(it)
    }

    Glide.with(context)
        .load(obj)
        .apply(requestOptions)
        .into(this)
}