package com.example.playlistmaker.search.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(
    private val data: MutableList<Track>,
    private val onTrackClickListener: OnTrackClickListener,
    private val onTrackLongClickListener: OnTrackLongClickListener? = null
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { onTrackClickListener.onTrackClick(data[holder.adapterPosition]) }
        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener?.onTrackLongClick(holder, holder.adapterPosition)
            true
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun clearTracks() {
        data.clear()
        notifyDataSetChanged()
    }

    fun removeTrack(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceTracks(addedTracks: List<Track>) {
        data.clear()
        data.addAll(addedTracks)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<Track> {
        return data
    }

}