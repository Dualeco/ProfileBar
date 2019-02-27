package com.dichotome.collapsingbar.ui.core

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.dichotome.collapsingbar.util.event.ClickThrottler
import com.dichotome.collapsingbar.util.event.Debounce

abstract class CoreLogic : ViewModel() {

    val clickThrottler = ClickThrottler()
    val debounce = Debounce(Handler())
}