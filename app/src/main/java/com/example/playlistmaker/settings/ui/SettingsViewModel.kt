package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.ThemeSettings
import com.example.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private var darkThemeLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().isDarkTheme)

    fun darkThemeLiveData(): LiveData<Boolean> = darkThemeLiveData

    fun updateBlackTheme(checked: Boolean) {
        val theme = ThemeSettings(checked)
        settingsInteractor.updateThemeSetting(theme)
        darkThemeLiveData.value = checked
    }

}
