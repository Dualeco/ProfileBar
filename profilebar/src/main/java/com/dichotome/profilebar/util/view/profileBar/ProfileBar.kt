package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.dichotome.profilebar.util.constant.Constants

class ProfileBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val toolbar: ProfileCollapsingToolbar = ProfileCollapsingToolbar(context)
) : AppBarLayout(context, attrs), ProfileBarInterface by toolbar {

    companion object {
        private const val SCROLL_FLAGS = SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
    }

    private val appbarHeight = Constants(context).DISPLAY_HEIGHT / 2
    private var heightSet = false

    fun setScrollFlags(flags: Int) {
        toolbar.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            scrollFlags = flags
        }
    }

    init {
        setScrollFlags(SCROLL_FLAGS)
        addView(toolbar)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!heightSet && measuredWidth > 0) {
            layoutParams = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                appbarHeight
            )
            heightSet = true
        }
    }
}