package com.example.playlistmaker.player.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.media.ui.playlist.recyclerview.OnPlaylistsClickListener
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.media.ui.playlist.recyclerview.PlaylistViewHolder


class MediaPlayerAdapter(
    private var data: List<Playlist>,
    private val onPlaylistClickListener: OnPlaylistsClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(data[position])
        }
    }

    fun updatePlaylists(newPlaylists: List<Playlist>) {
        data = newPlaylists
        notifyDataSetChanged()
    }
}

