package com.example.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.playlist_maker.OnPlaylistCreatedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), OnPlaylistCreatedListener {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val viewModel: PlaylistFragmentViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Установка слушателя нажатий на кнопку newPlaylist
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistMakerFragment)
        }
    }

    override fun onPlaylistCreated(playlistName: String) {
        binding.toast.isVisible = true
        val message = getString(R.string.playlist_created_message, playlistName)
        binding.toast.text = message
    }
}