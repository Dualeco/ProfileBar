package com.dichotome.profilebar.ui.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.dichotome.profilebar.ui.profileBar.toolbar.ProfileBarActions
import com.dichotome.profilebar.ui.profileBar.toolbar.ProfileToolbarAnimated
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

class ProfileBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val toolbar: ProfileToolbarAnimated = ProfileToolbarAnimated(
        context
    )
) : AppBarLayout(context, attrs),
    ProfileBarResources by toolbar,
    ProfileBarViews by toolbar,
    ProfileBarActions by toolbar {

    companion object {
        const val SCROLL_FLAGS = SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
    }


    fun setScrollFlags(flags: Int) {
        toolbar.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            scrollFlags = flags
        }
    }

    init {
        setScrollFlags(SCROLL_FLAGS)
        addView(toolbar)
    }
}