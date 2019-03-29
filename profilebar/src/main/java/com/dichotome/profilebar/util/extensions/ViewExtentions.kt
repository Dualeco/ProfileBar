package com.dichotome.profilebar.util.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout

fun ConstraintLayout.addViews(vararg children: View) {
    for (item in children) {
        addView(item)
    }
}