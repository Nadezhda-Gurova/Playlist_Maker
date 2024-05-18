package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.favoriteTracksModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playlistMakerModule
import com.example.playlistmaker.di.playlistsModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
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
    }
}