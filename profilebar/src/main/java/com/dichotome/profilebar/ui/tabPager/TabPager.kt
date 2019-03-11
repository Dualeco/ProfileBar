package com.dichotome.profilebar.ui.tabPager

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class TabPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private val fragmentManager = (context as AppCompatActivity).supportFragmentManager
    private var adapter: TabPagerAdapter = TabPagerAdapter(fragmentManager)
        set(value) {
            field = value
            super.setAdapter(field)
        }

    var fragments: List<TabFragment>?
        get() = adapter.tabFragments
        set(value) {
            value?.let {
                adapter = adapter?.apply {
                    tabFragments = it
                }
            }
        }
}