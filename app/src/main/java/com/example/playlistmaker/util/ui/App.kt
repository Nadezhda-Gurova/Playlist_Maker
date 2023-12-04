package com.example.playlistmaker.util.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(DARK_THEME_MODE, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(DARK_THEME_TEXT_KEY, darkTheme)

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val DARK_THEME_MODE = "dark_theme_mode"
        const val DARK_THEME_TEXT_KEY = "key_for_dark_theme"
    }
}