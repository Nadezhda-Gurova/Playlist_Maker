package com.example.playlistmaker.util.ui

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.provideSettingsInteractor(
            getSystemService(Context.UI_MODE_SERVICE) as UiModeManager,
            getSharedPreferences(DARK_THEME_MODE, MODE_PRIVATE)
        ).apply {
            updateThemeSetting(getThemeSettings())
        }
    }

    companion object {
        const val DARK_THEME_MODE = "dark_theme_mode"
        const val DARK_THEME_TEXT_KEY = "key_for_dark_theme"
    }
}