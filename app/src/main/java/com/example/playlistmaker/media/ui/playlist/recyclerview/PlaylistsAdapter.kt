package com.example.playlistmaker.media.ui.playlist.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemPlaylistBinding

class PlaylistsAdapter(
    private var playlists: List<Playlist>,
    private val onPlaylistClickListener: OnPlaylistsClickListener,
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            val playlistId = playlists.getOrNull(holder.bindingAdapterPosition)?.id
            playlistId?.let { id ->
                onPlaylistClickListener.onPlaylistClick(id)
            }
        }
    }

    fun replacePlaylists(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}