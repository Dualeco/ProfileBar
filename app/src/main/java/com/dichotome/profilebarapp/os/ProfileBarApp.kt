package com.dichotome.profilebarapp.os

import android.app.Application

class ProfileBarApp : Application() {

    companion object {

        lateinit var app: ProfileBarApp

    }

    override fun onCreate() {
        super.onCreate()

        app = this
    }
}