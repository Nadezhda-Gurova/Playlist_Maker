package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.vehicle_shop_clean.domain.consumer.Consumer
import com.example.vehicle_shop_clean.domain.consumer.ConsumerData
import java.util.concurrent.Executors

class GetTracksListUseCase(
    private val tracksRepository: TracksRepository,
) {
    private val executor = Executors.newCachedThreadPool()

    fun execute(trackName:String, consumer: Consumer<Track>){
        executor.execute {
            when (val iTunesResponse =
                tracksRepository.getTracksItunes(trackName)) {
                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(iTunesResponse.data.results))
                }

                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(iTunesResponse.message))
                }
            }
        }
    }
}