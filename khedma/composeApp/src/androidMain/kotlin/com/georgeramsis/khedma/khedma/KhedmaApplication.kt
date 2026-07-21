package com.georgeramsis.khedma.khedma

import android.app.Application
import com.georgeramsis.khedma.khedma.di.initKoin

class KhedmaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()  // ← called once when app process starts
    }
}