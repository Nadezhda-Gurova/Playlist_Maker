package com.example.playlistmaker.media.ui.media.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding

class PlaylistsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private var _binding: ItemPlaylistBinding? = null
    private val binding: ItemPlaylistBinding
        get() = _binding!!

    fun bind(playlist: PLaylists) {
        Glide.with(itemView)
            .load(playlist.imagePath)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.round_corner)))
            .into(binding.album)
        binding.description.text = playlist.name
        binding.numberOfTracks.text = playlist.trackCount
    }
}