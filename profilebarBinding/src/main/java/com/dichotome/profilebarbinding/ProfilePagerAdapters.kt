package com.dichotome.profilebarbinding

import androidx.databinding.BindingAdapter
import com.dichotome.profilebar.ui.TabPager
import com.dichotome.profilebar.ui.fragment.TabFragment

@BindingAdapter("app:fragments")
fun setFragments(tabPager: TabPager, fragments: List<TabFragment>?) {
    fragments?.let {
        tabPager.fragments = fragments
    }
}