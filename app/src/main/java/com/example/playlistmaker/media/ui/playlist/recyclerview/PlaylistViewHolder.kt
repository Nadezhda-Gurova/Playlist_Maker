package com.example.playlistmaker.media.ui.playlist.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding

class PlaylistViewHolder(private val binding: ItemPlaylistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.imagePath)
            .centerCrop()
            .placeholder(R.drawable.placeholder2)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.search_field)))
            .into(binding.album)
        binding.description.text = playlist.name
        binding.numberOfTracks.text = playlist.trackCount.toString()
        binding.textTracks.text = getTrackWordForm(playlist.trackCount)
    }
}

private fun getTrackWordForm(count: Int): String {
    val lastDigit = count % 10
    val lastTwoDigits = count % 100

    return when {
        lastTwoDigits in 11..19 -> " треков"
        lastDigit == 1 -> " трек"
        lastDigit in 2..4 -> " трека"
        else -> " треков"
    }
}