package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.ThemeSettings


interface DarkModeRepository {
    fun get(): ThemeSettings
    fun save(themeSettings: ThemeSettings)
}