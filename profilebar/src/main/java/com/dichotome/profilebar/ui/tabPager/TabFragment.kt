package com.dichotome.profilebar.ui.tabPager

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class TabFragment : Fragment() {
    companion object {
        const val ARGS_TITLE = "title"
    }
    var title: String
        get() = arguments?.getString(ARGS_TITLE) ?: ""
        set(title) {
            arguments = (arguments ?: Bundle()).apply {
                putString(ARGS_TITLE, title)
            }
        }
}