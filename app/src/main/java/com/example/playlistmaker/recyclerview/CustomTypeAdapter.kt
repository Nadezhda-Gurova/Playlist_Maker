package com.example.playlistmaker.recyclerview

import com.example.playlistmaker.data.TrackTime
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.Locale

class CustomTypeAdapter : TypeAdapter<TrackTime>() {

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun write(out: JsonWriter?, value: TrackTime) {
        out?.value(value.time)
    }
    override fun read(reader: JsonReader): TrackTime {
        return TrackTime(formatter.format(reader.nextLong()))
    }
}
