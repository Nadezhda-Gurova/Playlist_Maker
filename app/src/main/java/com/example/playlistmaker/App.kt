package com.example.playlistmaker

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.setApplication(this)

        Creator.provideSettingsInteractor(
            Creator.provideDarkModeRepository(getSystemService(Context.UI_MODE_SERVICE) as UiModeManager,)
        ).apply {
            updateThemeSetting(getThemeSettings())
        }
    }

    companion object {
        const val DARK_THEME_MODE = "dark_theme_mode"
        const val DARK_THEME_TEXT_KEY = "key_for_dark_theme"
    }
}