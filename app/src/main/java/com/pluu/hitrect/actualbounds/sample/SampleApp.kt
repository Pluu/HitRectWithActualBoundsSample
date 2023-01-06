package com.pluu.hitrect.actualbounds.sample

import android.app.Application
import logcat.AndroidLogcatLogger

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this)
    }
}