package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
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
        viewModel.onPreparedLiveData.observe(this) {
            binding.playButton.isEnabled = true
        }

        viewModel.onCompletionLiveData.observe(this) {
            binding.playButton.setImageResource(R.drawable.play_button)
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayerClicked()
        }

        viewModel.isShowPlayingLiveData.observe(this) {
            binding.playButton.setImageResource(if (it) R.drawable.play_button else R.drawable.pause_button)
        }

        viewModel.loadTrackData(track)

        viewModel.trackLiveData.observe(this) { loadedTrack ->
            setTrackData(loadedTrack)
            Glide.with(this)
                .load(loadedTrack.getCoverArtwork())
                .centerCrop()
                .placeholder(R.drawable.placeholder_album)
                .into(binding.albumCover)
        }

        if (binding.albumName.text != null) {
            binding.album.visibility = View.VISIBLE
            binding.albumName.visibility = View.VISIBLE
        }

        var isAddToPlaylistClicked = false

        binding.addToPlaylist.setOnClickListener {
            isAddToPlaylistClicked = if (!isAddToPlaylistClicked) {
                binding.addToPlaylist.setImageResource(R.drawable.button_add_to_playlist)
                true
            } else {
                binding.addToPlaylist.setImageResource(R.drawable.add_to_playlist)
                false
            }
        }

        var isAddToFavoritesClicked = false
        binding.addToFavorites.setOnClickListener {
            isAddToFavoritesClicked = if (!isAddToFavoritesClicked) {
                binding.addToFavorites.setImageResource(R.drawable.button_add_to_favorite)
                true
            } else {
                binding.addToFavorites.setImageResource(R.drawable.add_to_favorites)
                false
            }
        }

        viewModel.playingProgressLiveData.observe(this) {
            binding.time.text = it
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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    private fun initBackButton() {
        val back = findViewById<ImageView>(R.id.back_button)

        back.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}