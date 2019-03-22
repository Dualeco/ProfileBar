package com.dichotome.profilebar.ui.tabPager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.viewpager.widget.ViewPager

class TabPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    var adapter: TabPagerAdapter? = null
        get() {
            return super.getAdapter().let {
                if (it is TabPagerAdapter)
                    it
                else
                    null
            }
        }
        set(value) {
            field = value?.apply {
                tabFragments = fragments
            }
            super.setAdapter(field)
        }

    var fragments: List<TabFragment> = arrayListOf()
        set(value) {
            field = value
            adapter = adapter?.apply {
                tabFragments = value
            }
        }
}
