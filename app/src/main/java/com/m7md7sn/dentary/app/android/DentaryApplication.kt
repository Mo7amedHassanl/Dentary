package com.m7md7sn.dentary.app.android

import android.app.Application
import android.content.res.Configuration
import com.m7md7sn.dentary.utils.LocaleUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DentaryApplication: Application() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.applyLocale(this)
    }
}