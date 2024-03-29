package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity.Companion.TRACK_MEDIA
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaPlayerActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: MediaPlayerViewModel by viewModel {
        parametersOf(getString(R.string.zero_time))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        binding = ActivityAudioPlayerBinding.bind(findViewById(R.id.root))

        initBackButton()

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_MEDIA, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_MEDIA) as? Track
        }

        requireNotNull(track) { "No track provided" }

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