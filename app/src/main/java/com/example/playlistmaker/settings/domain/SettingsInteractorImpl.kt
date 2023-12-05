package com.example.playlistmaker.settings.domain

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.ThemeSettings
import com.example.playlistmaker.util.ui.App

class SettingsInteractorImpl(
    private val sharedPrefs: SharedPreferences
) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPrefs.getBoolean(App.DARK_THEME_TEXT_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })
    }
}
