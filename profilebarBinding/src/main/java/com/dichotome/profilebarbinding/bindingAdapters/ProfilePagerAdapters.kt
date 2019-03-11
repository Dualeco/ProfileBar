package com.dichotome.profilebarbinding.bindingAdapters

import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.ui.tabPager.TabPager
import com.dichotome.profilebar.ui.tabPager.TabFragment

@BindingAdapter("app:fragments")
fun setFragments(tabPager: TabPager, fragments: List<TabFragment>?) {
    fragments?.let {
        tabPager.fragments = fragments
    }
}