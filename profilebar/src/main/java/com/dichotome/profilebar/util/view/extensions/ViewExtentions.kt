package com.dichotome.profilebar.util.view.extensions

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

fun ConstraintLayout.addChildren(collection: Collection<View>) {
    collection.forEach {
        addView(it)
    }
}