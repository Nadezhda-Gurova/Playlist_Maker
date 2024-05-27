package com.example.playlistmaker.media.ui.playlist_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding: FragmentPlaylistDetailsBinding
        get() = _binding!!

    private val playlists = arrayListOf<Playlist>()
    private val viewModel: PlaylistDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackId = requireArguments().getString("trackId")?.toIntOrNull()
        trackId?.let { viewModel.loadPlaylist(it) }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.uiStateLiveData.observe(this) {uiState ->
            setPlaylistData(uiState)
            Glide.with(this)
                .load(uiState.imagePath)
                .centerCrop()
                .placeholder(R.drawable.placeholder_album)
                .into(binding.playlistCover)}
    }

    private fun setPlaylistData(uiState: PlaylistUIState) {
        binding.playlistName.text = uiState.name
        binding.playlistDescription.text = uiState.description
        binding.minutes.text = uiState.totalDuration
        binding.tracksCount.text = uiState.trackCount.toString()

    }
}