package com.tapan.avomatest

import android.app.Application
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import com.pixplicity.easyprefs.library.Prefs
import com.tapan.avomatest.ui.SettingsFragment
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RSSApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this@RSSApp


        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        checkDarkMode()
    }

    private fun checkDarkMode() {
        if (Prefs.getBoolean(SettingsFragment.DARK_MODE, false)) {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_YES
                );
        } else {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_NO
                );
        }
    }


    companion object {
        lateinit var instance: Application
            private set
    }
}