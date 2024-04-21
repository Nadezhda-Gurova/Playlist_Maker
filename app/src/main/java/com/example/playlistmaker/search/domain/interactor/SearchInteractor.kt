package com.example.playlistmaker.search.domain.interactor

import androidx.core.util.Consumer
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.util.LoadingState
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchInteractor(
    private val tracksRepository: TracksRepository,
) {

    fun execute(trackName: String): Flow<LoadingState<List<Track>>> {
        return tracksRepository.getTracks(trackName)
        }
    }
