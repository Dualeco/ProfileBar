package com.dichotome.profilebarbinding.viewContentAdapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dichotome.profilebar.stubFragments.TabFragment

class TabPagerAdapter(fm: FragmentManager, var tabFragments: List<TabFragment> = arrayListOf()) :
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int = tabFragments.size

    override fun getItem(position: Int): TabFragment = tabFragments[position]

    override fun getPageTitle(position: Int): String = tabFragments[position].title ?: ""
}