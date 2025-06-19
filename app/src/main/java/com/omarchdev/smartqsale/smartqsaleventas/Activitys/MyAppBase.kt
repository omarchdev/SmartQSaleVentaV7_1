package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.app.Application
import java.util.Locale

class MyAppBase: Application() {

    override fun onCreate() {
        super.onCreate()
        setLocale(Locale.US)
    }

    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}