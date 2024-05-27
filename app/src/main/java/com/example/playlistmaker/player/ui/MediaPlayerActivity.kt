package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerFragment
import com.example.playlistmaker.player.ui.recyclerview.MediaPlayerAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFragment.Companion.TRACK_MEDIA
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaPlayerActivity : AppCompatActivity(), PreviousFragmentCallBack, PlaylistCreationListener {
    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: MediaPlayerViewModel by viewModel {
        parametersOf(getString(R.string.zero_time))
    }

    private lateinit var playlistAdapter: MediaPlayerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        binding = ActivityAudioPlayerBinding.bind(findViewById(R.id.root1))


        binding.newPlaylist.setOnClickListener {
            binding.fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlaylistMakerFragment())
                .addToBackStack(null)
                .commit()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility = if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })
        initBackButton()

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_MEDIA, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_MEDIA) as? Track
        }
        requireNotNull(track) { "No track provided" }

        playlistAdapter = MediaPlayerAdapter(arrayListOf()) { playlist ->
            viewModel.addTrackToPlaylist(track, playlist)
        }
        binding.recyclerView.adapter = playlistAdapter

        viewModel.playlists.observe(this) { playlist ->
            playlistAdapter.updatePlaylists(playlist)
        }

        viewModel.addTrackStatus.observe(this) { status ->
            when (status) {
                is AddTrackStatus.Success -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        this,
                        "Добавлено в плейлист ${status.playlist.name}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                is AddTrackStatus.AlreadyExists -> {
                    Toast.makeText(this, "Трек уже присутствует в плейлисте", Toast.LENGTH_SHORT)
                        .show()
                }

                is AddTrackStatus.Error -> {
                    Toast.makeText(
                        this,
                        "Ошибка при добавлении трека в плейлист",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }

        viewModel.uiStateLiveData.observe(this) {
            setTrackData(it.curTrack)
            Glide.with(this)
                .load(it.curTrack.getCoverArtwork())
                .centerCrop()
                .placeholder(R.drawable.placeholder_album)
                .into(binding.albumCover)

            if (it.isPausePlaying) {
                binding.playButton.setImageResource(R.drawable.play_button)
            } else {
                binding.playButton.setImageResource(R.drawable.pause_button)
            }

            if (it.isAddedToFavorites) {
                binding.addToFavorites.setImageResource(R.drawable.favorite)
            } else {
                binding.addToFavorites.setImageResource(R.drawable.unfavorite)
            }

            if (it.isAddedToPlaylist) {
                binding.addToPlaylist.setImageResource(R.drawable.added_to_playlist)
            } else {
                binding.addToPlaylist.setImageResource(R.drawable.removed_to_playlist)
            }

            if (it.isReady) {
                binding.playButton.isEnabled = true
            }

            binding.time.text = it.curTime

        }

        viewModel.loadTrackData(track)

        binding.playButton.setOnClickListener {
            viewModel.onPlayerClicked()
        }

        binding.addToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.onPlaylistClicked()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.onFavoriteClicked()
            }
        }
    }

    private fun setTrackData(track: Track) {
        binding.nameOfSong.text = track.trackName
        binding.authorOfSong.text = track.artistName
        binding.durationMinutes.text = track.trackTime
        binding.albumYear.text = track.releaseDate?.substring(0, 4)
        binding.albumCountry.text = track.country
        binding.albumName.text = track.collectionName
        binding.albumGenre.text = track.primaryGenreName

        if (track.collectionName != null) {
            binding.album.isVisible = true
            binding.albumName.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    private fun initBackButton() {
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onPreviousFragmentCreation() {
        binding.fragmentContainer.visibility = View.GONE
    }

    override fun onPlaylistCreated() {
        viewModel.onRestorePlaylists()
    }
}


