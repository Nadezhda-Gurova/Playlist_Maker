package com.example.playlistmaker.search.data.adapter

import com.example.playlistmaker.search.data.dto.TrackTime
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.Locale

class TrackTypeAdapter : TypeAdapter<TrackTime>() {

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun write(out: JsonWriter?, value: TrackTime) {
        out?.value(value.time)
    }
    override fun read(reader: JsonReader): TrackTime {
        return TrackTime(formatter.format(reader.nextLong()))
    }
}