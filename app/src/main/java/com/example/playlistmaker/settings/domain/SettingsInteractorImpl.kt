package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(
    private val darkModeRepository: DarkModeRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return darkModeRepository.get()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        darkModeRepository.save(settings)
    }
}
