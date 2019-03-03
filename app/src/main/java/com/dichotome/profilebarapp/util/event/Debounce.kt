package com.dichotome.profilebarapp.util.event

import android.os.Handler

class Debounce(private val handler: Handler, val delay: Long = DEFAULT_DELAY) {

    companion object {

        const val DEFAULT_DELAY = 350L
    }

    private var lastAction: (() -> Unit)? = null

    private val runnable = Runnable {
        lastAction?.invoke()
        lastAction = null
    }

    fun offer(action: () -> Unit) {
        val run = (lastAction == null)
        lastAction = action
        if (run) handler.postDelayed(runnable, delay)
    }
}