package com.dichotome.profilebar.util.extensions

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

fun ConstraintLayout.addViews(vararg children: View) {
    for (item in children) {
        addView(item)
    }
}