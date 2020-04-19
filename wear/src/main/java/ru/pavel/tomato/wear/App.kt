package ru.pavel.tomato.wear

import android.app.Application
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("DEBUG", "onCreate")
    }

}