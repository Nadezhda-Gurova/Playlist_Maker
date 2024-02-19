package com.example.playlistmaker.settings.domain


interface DarkModeRepository {
    fun get(): ThemeSettings
    fun save(themeSettings: ThemeSettings)
}