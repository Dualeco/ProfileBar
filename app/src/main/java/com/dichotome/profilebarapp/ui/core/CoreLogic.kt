package com.dichotome.profilebarapp.ui.core

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.dichotome.profilebarapp.util.event.ClickThrottler
import com.dichotome.profilebarapp.util.event.Debounce

abstract class CoreLogic : ViewModel() {

    val clickThrottler = ClickThrottler()
    val debounce = Debounce(Handler())
}