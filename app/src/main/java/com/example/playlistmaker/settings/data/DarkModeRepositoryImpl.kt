package com.example.playlistmaker.settings.data

import android.app.UiModeManager
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.DarkModeRepository
import com.example.playlistmaker.settings.domain.ThemeSettings

class DarkModeRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val uiModeManager: UiModeManager,
) :
    DarkModeRepository {
    override fun get(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(DARK_THEME_TEXT_KEY, false))
    }

    override fun save(themeSettings: ThemeSettings) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(
                if (themeSettings.isDarkTheme) {
                    UiModeManager.MODE_NIGHT_YES
                } else {
                    UiModeManager.MODE_NIGHT_NO
                }
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (themeSettings.isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        sharedPreferences.edit { putBoolean(DARK_THEME_TEXT_KEY, themeSettings.isDarkTheme) }
    }

    companion object {
        const val DARK_THEME_MODE = "dark_theme_mode"
        const val DARK_THEME_TEXT_KEY = "key_for_dark_theme"
    }
}