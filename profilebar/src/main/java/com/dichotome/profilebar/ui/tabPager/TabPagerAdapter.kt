package com.dichotome.profilebar.ui.tabPager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dichotome.profilebar.stubs.fragments.TabFragment

class TabPagerAdapter(fm: FragmentManager, var tabFragments: List<TabFragment> = mutableListOf()) :
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int = tabFragments.size

    override fun getItem(position: Int): TabFragment = tabFragments[position]

    override fun getPageTitle(position: Int): String = tabFragments[position].title

    fun setPageTitle(index: Int, title: String) {
        tabFragments[index].title = title
    }

    fun addTab(fragment: TabFragment) {
        tabFragments += fragment
    }

    fun removeTab(fragment: TabFragment) {
        tabFragments.apply {
        for (i in 0 until size)
            if (get(i) == fragment) {
                tabFragments = subList(0, i) + subList(i + 1, tabFragments.size)
                return
            }
        }
    }

}