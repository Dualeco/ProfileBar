package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.tabs.TabLayout

class ProfileTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TabLayout(context, attrs, defStyle) {
    var enabled: Boolean? = null
        set(value) {
            field = value
            visibility = setVisible(value)
        }

    private fun setVisible(boolean: Boolean?) = if (boolean == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}