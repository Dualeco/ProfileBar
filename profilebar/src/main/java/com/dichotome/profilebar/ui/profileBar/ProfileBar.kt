package com.dichotome.profilebar.ui.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.dichotome.profilebar.ui.profileBar.toolbar.ProfileAnimatedToolbar
import com.dichotome.profileshared.constants.Constants
import com.dichotome.profileshared.extensions.dpToPx
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

class ProfileBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val toolbar: ProfileAnimatedToolbar = ProfileAnimatedToolbar(
        context
    )
) : AppBarLayout(context, attrs),
    ProfileBarResources by toolbar,
    ProfileBarViews by toolbar {

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