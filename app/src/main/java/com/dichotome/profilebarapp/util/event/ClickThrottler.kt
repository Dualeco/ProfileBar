package com.dichotome.profilebarapp.util.event

class ClickThrottler(private val delay: Long = DEFAULT_DELAY) {

    companion object {

        const val DEFAULT_DELAY = 550L
    }

    private var lastOffer = 0L

    fun next(action: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastOffer > delay) {
            lastOffer = now
            action()
        }
    }
}