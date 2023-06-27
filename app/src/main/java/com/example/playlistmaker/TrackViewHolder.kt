package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val albumImage: ImageView = itemView.findViewById(R.id.album)
    private val songText: TextView = itemView.findViewById(R.id.song_name)
    private val authorText: TextView = itemView.findViewById(R.id.author)
    private val timeText: TextView = itemView.findViewById(R.id.time)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(2))
            .into(albumImage)
        songText.text = model.trackName
        authorText.text = model.artistName
        timeText.text = model.trackTime.toString()

    }

}