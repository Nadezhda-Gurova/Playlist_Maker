package com.example.playlistmaker.player.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistAudioPlayerBinding
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist

class MediaPlayerViewHolder(private val binding: ItemPlaylistAudioPlayerBinding) :
    RecyclerView.ViewHolder(binding.rootItemBottomsheet) {
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.numberOfTracks.text = playlist.trackCount.toString()
        Glide.with(itemView)
            .load(playlist.imagePath)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.round_corner)))
            .into(binding.album)
    }
}