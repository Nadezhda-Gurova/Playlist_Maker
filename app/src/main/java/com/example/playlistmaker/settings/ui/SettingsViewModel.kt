package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.settings.data.ThemeSettings
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private var darkThemeLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().isDarkTheme)

    fun darkThemeLiveData(): LiveData<Boolean> = darkThemeLiveData

    fun shareData() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun updateBlackTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(checked))
        darkThemeLiveData.value = checked
    }

    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(
                        sharingInteractor,
                        settingsInteractor
                    )
                }
            }
    }
}
