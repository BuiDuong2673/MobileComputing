package com.codemave.phoneboss

import android.app.Application

//This application class sets up our dependency [Graph] with a context
class PhoneBossApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}