package com.example.playlistmaker.media.ui.playlist_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private var _playlistId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getInt("trackId")
        _playlistId = playlistId
        viewModel.loadPlaylist(playlistId)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { uiState ->
            setPlaylistData(uiState)
            setPlaylistDataInBottomSheet(uiState)
            Glide.with(this)
                .load(uiState.imagePath)
                .centerCrop()
                .placeholder(R.drawable.placeholder_album)
                .into(binding.playlistCover)
        }
        viewModel.tracksLiveData.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.replaceTracks(tracks)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        View.GONE
                    } else {
                        View.GONE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
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

        val sheetMenuBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        sheetMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        binding.share.setOnClickListener {
            sharePlaylist()
        }

        binding.edit.setOnClickListener {
            val action =
                PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToEditPlaylistFragment(
                    playlistId = _playlistId
                )
            findNavController().navigate(action)
        }

        binding.menuIc.setOnClickListener {
            if (sheetMenuBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                sheetMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                sheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }


        trackAdapter = TrackAdapter(arrayListOf(), onTrackClickListener, onTrackLongClickListener)
        binding.recyclerView.adapter = trackAdapter
        binding.deletePanel.setOnClickListener {
            deletePlaylistConfirmationDialog()
        }


    }

    private fun sharePlaylist() {
        val tracks = viewModel.tracksLiveData.value
        if (tracks.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val playlist = viewModel.uiStateLiveData.value
        if (playlist == null) {
            Toast.makeText(
                requireContext(),
                "Информация о плейлисте недоступна",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val playlistInfo = buildString {
            appendLine(playlist.name)
            appendLine(playlist.description)
            appendLine("${playlist.trackCount} треков")
        }

        val tracksInfo = buildString {
            tracks.forEachIndexed { index, track ->
                appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})")
            }
        }
        val message = "$playlistInfo\n$tracksInfo"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Поделиться плейлистом"))
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

    private fun deletePlaylistConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Да") { dialog, _ ->
                viewModel.deletePlaylist(_playlistId)
                navigateToMedia()
            }
            .show()
    }

    private fun navigateToMedia() {
        findNavController().navigate(R.id.action_playlistDetailsFragment_to_mediaFragment)
    }

    private fun setPlaylistDataInBottomSheet(uiState: PlaylistUIState) {

        Glide.with(this)
            .load(uiState.imagePath)
            .centerCrop()
            .placeholder(R.drawable.placeholder_album)
            .into(binding.album)

        binding.curPlaylistName.text = uiState.name
        binding.trackCountBs.text = uiState.trackCount.toString()
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