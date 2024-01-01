package com.example.playlistmaker.settings.ui

import android.app.UiModeManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.DarkModeRepository
import com.example.playlistmaker.settings.domain.ThemeSettings
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(
    private val uiModeManager: UiModeManager,
    private val darkModeRepository: DarkModeRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return darkModeRepository.get()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        darkModeRepository.save(settings)

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
