package com.example.playlistmaker.media.ui.playlist_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.player.ui.MediaPlayerActivity
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.search.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.search.ui.recyclerview.OnTrackLongClickListener
import com.example.playlistmaker.search.ui.recyclerview.TrackAdapter
import com.example.playlistmaker.search.ui.recyclerview.TrackViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding: FragmentPlaylistDetailsBinding
        get() = _binding!!

    private val viewModel: PlaylistDetailsViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackId = requireArguments().getInt("trackId")
        viewModel.loadPlaylist(trackId)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.uiStateLiveData.observe(this) { uiState ->
            setPlaylistData(uiState)
            Glide.with(this)
                .load(uiState.imagePath)
                .centerCrop()
                .placeholder(R.drawable.placeholder_album)
                .into(binding.playlistCover)
        }
        viewModel.tracksLiveData.observe(this) { tracks ->
            trackAdapter.replaceTracks(tracks)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlayTracks.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlayTracks.alpha = slideOffset
            }
        })
        val onTrackClickListener = OnTrackClickListener { track ->
            val intent = Intent(requireContext(), MediaPlayerActivity::class.java)
            intent.putExtra(SearchFragment.TRACK_MEDIA, track)
            startActivity(intent)
        }

        val onTrackLongClickListener = object : OnTrackLongClickListener {
            override fun onTrackLongClick(holder: TrackViewHolder, position: Int) {
                showDeleteConfirmationDialog(holder, position)
            }
        }

        trackAdapter = TrackAdapter(arrayListOf(), onTrackClickListener, onTrackLongClickListener)
        binding.recyclerView.adapter = trackAdapter


    }
    private fun showDeleteConfirmationDialog(holder: TrackViewHolder, position: Int) {
        val track = trackAdapter.getData()[position]
        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setPositiveButton("Удалить") { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track)
                trackAdapter.removeTrack(position)
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setPlaylistData(uiState: PlaylistUIState) {
        binding.playlistName.text = uiState.name
        binding.playlistDescription.text = uiState.description
        binding.minutes.text = uiState.totalDuration
        binding.tracksCount.text = uiState.trackCount.toString()
        if (uiState.description != "") {
            binding.playlistDescription.isVisible = true
        }
    }
}