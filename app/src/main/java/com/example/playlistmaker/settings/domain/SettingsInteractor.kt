package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.data.ThemeSettings


interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings) {}
}

class SI : SettingsInteractor{
    override fun getThemeSettings(): ThemeSettings {
        TODO("Not yet implemented")
    }
}

class X {

    fun lol2 (){
        val xx: SettingsInteractor? = SI()
        println(xx?.getThemeSettings())
    }

    fun lol (){
        val xx: Int? = 9
        println(xx?.toByte())
    }
}