package com.example.playlistmaker.media.ui.media.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class NewsAdapter(
    private val news: List<PLaylists>,
    private val onPlaylistClickListener: OnPlaylistsClickListener
) : RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_playlist, parent, false
            )
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(news[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(news[holder.adapterPosition])
        }
    }
}