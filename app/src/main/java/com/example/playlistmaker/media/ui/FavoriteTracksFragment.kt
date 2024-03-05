package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media.ui.recyclerview.MediaTrackAdapter
import com.example.playlistmaker.media.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.player.ui.MediaPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        }

    private val tracks = arrayListOf<Track>()
    private lateinit var binding: FragmentFavoriteTracksBinding
//    private val viewModel: FavoriteTracksViewModel by viewModel()
    private lateinit var trackAdapter: MediaTrackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(FavoriteTracksViewModel::class.java)

//        val onTrackClickListener = OnTrackClickListener { track ->
//            viewModel.addTrack(track)
//                val intent = Intent(requireContext(), MediaPlayerActivity::class.java)
//                startActivity(intent)
//
//        }

//        trackAdapter = MediaTrackAdapter(tracks, onTrackClickListener)
//
//        // Наблюдение за loadingState и вызов renderTracks
//        viewModel.loadingState.observe(viewLifecycleOwner) { loadingState ->
//            renderTracks(loadingState)
//        }
        return binding.root
    }

    private fun renderTracks(loadingState: List<Track>?) {
        if (loadingState != null) {
            if (loadingState.isEmpty()) {
                binding.nothingFound.isVisible = true
                binding.noPlaylistCreated.isVisible = true
            } else {
                // Установите адаптер и передайте список треков
                trackAdapter.submitList(loadingState)

                // Настраиваем RecyclerView
                binding.recyclerViewMedia.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = trackAdapter
                }

            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}