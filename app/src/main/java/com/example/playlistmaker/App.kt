package com.example.playlistmaker

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.favoriteTracksModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playlistMakerModule
import com.example.playlistmaker.di.playlistsModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.data.DarkModeRepositoryImpl
import com.example.playlistmaker.settings.data.DarkModeRepositoryImpl.Companion.DARK_THEME_MODE
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule,
                mediaModule,
                favoriteTracksModule,
                playlistsModule,
                playlistMakerModule
            )
        }
        val darkModeRepository = DarkModeRepositoryImpl(
            sharedPreferences = getSharedPreferences(DARK_THEME_MODE, MODE_PRIVATE),
            uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        )
        val themeSettings = darkModeRepository.get()
        darkModeRepository.save(themeSettings)
    }
}