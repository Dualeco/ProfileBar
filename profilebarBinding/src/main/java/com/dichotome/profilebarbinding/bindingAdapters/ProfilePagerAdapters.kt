package com.dichotome.profilebarbinding.bindingAdapters

import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.ui.TabPager
import com.dichotome.profilebar.stubFragments.TabFragment

@BindingAdapter("app:fragments")
fun setFragments(tabPager: TabPager, fragments: List<TabFragment>?) {
    fragments?.let {
        tabPager.fragments = fragments
    }
}