package com.dichotome.profilebar.util.view.profileBar

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout

class ProfileTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TabLayout(context, attrs, defStyle) {
    var enabled: Boolean? = null
        set(value) {
            field = value
            isVisible = value ?: false
        }
}