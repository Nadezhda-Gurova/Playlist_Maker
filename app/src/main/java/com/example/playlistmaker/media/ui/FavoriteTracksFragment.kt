package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.player.ui.MediaPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.search.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.search.ui.recyclerview.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding: FragmentFavoriteTracksBinding
        get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter
    private val tracks = arrayListOf<Track>()
    private lateinit var clickDebounce: (Track) -> Unit

    private val viewModel: FavoriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.favoriteTracksLoadingState.observe(viewLifecycleOwner) { loadingState ->
            renderTracks(loadingState)
        }

        clickDebounce = debounce(
            1000L,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val intent = Intent(requireContext(), MediaPlayerActivity::class.java)
            intent.putExtra(SearchFragment.TRACK_MEDIA, track)
            startActivity(intent)
        }

        val onTrackClickListener = OnTrackClickListener { track ->
            clickDebounce(track)
        }


        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        binding.recyclerViewMedia.adapter = trackAdapter
        viewModel.favoriteTracksLoadingState.observe(viewLifecycleOwner) { loadingState ->
            renderTracks(loadingState)
        }

    }


    private fun renderTracks(loadingState: FavoriteTracksState?) {
        when (loadingState) {
            is FavoriteTracksState.Content -> {
                hideNothingAdded(false)
                trackAdapter.clearTracks()
                trackAdapter.replaceTracks(loadingState.tracks)

            }

            is FavoriteTracksState.Empty -> {
                trackAdapter.clearTracks()
                hideNothingAdded(true)
            }

            else -> {}
        }
    }

    private fun hideNothingAdded(visible: Boolean) {
        binding.nothingFound.isVisible = visible
        binding.noPlaylistCreated.isVisible = visible
    }

}