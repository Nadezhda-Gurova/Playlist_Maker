package com.example.playlistmaker.domain.use_case

import androidx.core.util.Consumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.util.LoadingState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GetTracksListUseCase(
    private val tracksRepository: TracksRepository,
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
) {

    fun execute(trackName: String, consumer: Consumer<LoadingState<List<Track>>>) {
        executorService.execute {
            consumer.accept(tracksRepository.getTracks(trackName))
        }
    }
}