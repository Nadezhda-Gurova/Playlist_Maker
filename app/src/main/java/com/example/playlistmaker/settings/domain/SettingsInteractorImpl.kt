package com.example.playlistmaker.settings.domain

import android.app.UiModeManager
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.ThemeSettings
import com.example.playlistmaker.util.ui.App
import com.example.playlistmaker.util.ui.App.Companion.DARK_THEME_TEXT_KEY

class SettingsInteractorImpl(
    private val uiModeManager: UiModeManager,
    private val sharedPrefs: SharedPreferences
) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPrefs.getBoolean(App.DARK_THEME_TEXT_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_TEXT_KEY, settings.isDarkTheme)
            .apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (settings.isDarkTheme) {
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
            } else {
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (settings.isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}
