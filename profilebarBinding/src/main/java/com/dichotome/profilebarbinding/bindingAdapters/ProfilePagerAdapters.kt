package com.dichotome.profilebarbinding.bindingAdapters

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager

@BindingAdapter("app:offscreenPageLimit")
fun setOffscreenPageLimit(viewPager: ViewPager, pages: Int?) {
    pages?.let {
        viewPager.offscreenPageLimit = pages
    }
}