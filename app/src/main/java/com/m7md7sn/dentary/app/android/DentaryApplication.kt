package com.m7md7sn.dentary.app.android

import android.app.Application
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.m7md7sn.dentary.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DentaryApplication: Application() {
    override fun onCreate() {
        super.onCreate()

    }
}