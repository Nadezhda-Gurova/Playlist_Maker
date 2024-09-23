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
import com.example.playlistmaker.media.ui.media.MediaFragmentDirections
import com.example.playlistmaker.media.ui.playlist.recyclerview.OnPlaylistsClickListener
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.media.ui.playlist.recyclerview.PlaylistsAdapter
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val playlists = arrayListOf<Playlist>()
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
        viewModel.onRestore()

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistMakerFragment)
        }

        val onPlaylistClickListener = OnPlaylistsClickListener { playlist ->
            val action =
               MediaFragmentDirections.actionMediaFragmentToPlaylistDetailsFragment(playlist.id)
            findNavController().navigate(action)
        }

        playlistAdapter = PlaylistsAdapter(playlists, onPlaylistClickListener)
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding.recyclerView.adapter = playlistAdapter
        viewModel.playlists.observe(viewLifecycleOwner) { playlistState ->
            renderPlaylists(playlistState)
        }
    }

    private fun renderPlaylists(curPlaylists: List<Playlist>) {
        nothingAddedVisability(curPlaylists.isEmpty())
        if (curPlaylists.isNotEmpty()) playlistAdapter.replacePlaylists(curPlaylists)
    }

    fun nothingAddedVisability(visible: Boolean) {
        binding.nothingFound.isVisible = visible
        binding.noPlaylistCreated.isVisible = visible
    }

    override fun onDestroyView() {
        viewModel.onDestroy()
        super.onDestroyView()
        _binding = null
    }
}