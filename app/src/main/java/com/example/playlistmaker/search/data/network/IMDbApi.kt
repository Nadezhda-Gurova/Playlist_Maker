package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IMDbApi {
    @GET("/search?entity=song ")
    suspend fun search(@Query("term") text: String): ITunesResponse
}