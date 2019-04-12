package com.dichotome.profilebar.util.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.download(source: Any?, options: RequestOptions? = null) {
    Glide.with(context)
        .load(source)
        .apply((options ?: RequestOptions()).apply { placeholder(drawable) })
        .into(this)
}