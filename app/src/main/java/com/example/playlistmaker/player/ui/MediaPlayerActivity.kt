package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity.Companion.TRACK_MEDIA

class MediaPlayerActivity : ComponentActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var viewModel: MediaPlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        binding = ActivityAudioPlayerBinding.bind(findViewById(R.id.root))

        val mediaPlayer = MediaPlayer()

        viewModel = ViewModelProvider(
            this,
            MediaPlayerViewModel.getViewModelFactory(
                Creator.provideMediaPlayerInteractor(mediaPlayer),
                Creator.provideTimerInteractor(
                    mediaPlayer,
                    Handler(Looper.getMainLooper()),
                    Creator.provideSimpleDateFormat(),
                    this
                )
            )
        )[MediaPlayerViewModel::class.java]

        initBackButton()

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_MEDIA, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_MEDIA) as? Track
        }

        requireNotNull(track) { "No track provided" }

        viewModel.uiStateLiveData.observe(this) {
            when (it) {
                is UiState.CurrentTrack -> {
                    setTrackData(it.track)
                    Glide.with(this)
                        .load(it.track.getCoverArtwork())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_album)
                        .into(binding.albumCover)
                }

                UiState.PausePlaying -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)
                }

                UiState.Prepared -> binding.playButton.isEnabled = true
                is UiState.Progress -> binding.time.text = it.time
                UiState.ShowPlaying, UiState.Completed -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                }

                UiState.AddedToFavorites -> binding.addToFavorites.setImageResource(R.drawable.favorite)
                UiState.RemovedFromFavorites -> binding.addToFavorites.setImageResource(R.drawable.unfavorite)
                UiState.AddedToPlaylist -> binding.addToPlaylist.setImageResource(R.drawable.added_to_playlist)
                UiState.RemovedFromPlaylist -> binding.addToPlaylist.setImageResource(R.drawable.removed_to_playlist)
            }
        }

        viewModel.loadTrackData(track)

        binding.playButton.setOnClickListener {
            viewModel.onPlayerClicked()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.addToPlaylist()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.addToFavorites()
        }

    }

    private fun setTrackData(track: Track) {
        binding.nameOfSong.text = track.trackName
        binding.authorOfSong.text = track.artistName
        binding.durationMinutes.text = track.trackTime
        binding.albumYear.text = track.releaseDate.substring(0, 4)
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
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}