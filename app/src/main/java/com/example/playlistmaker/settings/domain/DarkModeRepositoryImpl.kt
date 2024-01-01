package com.example.playlistmaker.settings.domain

import android.content.SharedPreferences
import com.example.playlistmaker.App.Companion.DARK_THEME_TEXT_KEY
import com.example.playlistmaker.settings.data.DarkModeRepository

class DarkModeRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    DarkModeRepository {
    override fun get(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(DARK_THEME_TEXT_KEY, false))
    }

    override fun save(themeSettings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_TEXT_KEY, themeSettings.isDarkTheme)
            .apply()
    }
}