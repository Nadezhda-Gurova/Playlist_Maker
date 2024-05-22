package com.example.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.playlist.recyclerview.OnPlaylistsClickListener
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.media.ui.playlist.recyclerview.PlaylistsAdapter
import com.example.playlistmaker.media.ui.playlist_maker.OnPlaylistCreatedListener
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerViewModel
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), OnPlaylistCreatedListener {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val playlists  = arrayListOf<Playlist>()
    private val viewModel: PlaylistMakerViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка слушателя нажатий на кнопку newPlaylist
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistMakerFragment)
        }

        val onPlaylistClickListener = OnPlaylistsClickListener { playlist ->
//            viewModel.addPlaylist(playlist)
//            clickDebounce(track)
        }

        playlistAdapter = PlaylistsAdapter(playlists, onPlaylistClickListener)
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(), /*Количество столбцов*/
            2
        ) //ориентация по умолчанию — вертикальная
        binding.recyclerView.adapter = playlistAdapter

        viewModel.playlists.observe(viewLifecycleOwner) { playlistState ->
            renderPlaylists(playlistState)

        }
    }

    private fun renderPlaylists(loadingState: PlaylistsState) {
        when (loadingState) {
            is PlaylistsState.Content -> {
                nothingAddedVisability(false)
                playlistAdapter.clearPlaylists()
                playlistAdapter.replacePlaylists(loadingState.playlists)
            }

            is PlaylistsState.Empty -> {
               playlistAdapter.clearPlaylists()
                nothingAddedVisability(true)
            }
        }
    }

    fun nothingAddedVisability(visible: Boolean) {
        binding.nothingFound.isVisible = visible
        binding.noPlaylistCreated.isVisible = visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPlaylistCreated(playlistName: String) {
//        binding.toast.isVisible = true
//        val message = getString(R.string.playlist_created_message, playlistName)
//        binding.toast.text = message
    }
}