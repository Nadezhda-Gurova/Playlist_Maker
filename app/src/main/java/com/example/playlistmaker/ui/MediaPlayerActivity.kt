package com.example.playlistmaker.ui

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.SearchActivity.Companion.TRACK_MEDIA
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private val handler = Handler(Looper.getMainLooper())
    private var playerState: PlayerState = PlayerState.NotInited
    private val mediaPlayer = MediaPlayer()

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

        setTrackData(track)

        if (binding.albumName.text != null) {
            binding.album.visibility = View.VISIBLE
            binding.albumName.visibility = View.VISIBLE
        }

        Glide.with(this)
            .load(getCoverArtwork(track))
            .centerCrop()
            .placeholder(R.drawable.placeholder_album)
            .into(binding.albumCover)

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

        val url = track.previewUrl
        preparePlayer(url)

        binding.playButton.setOnClickListener {
            switchPlayerState()
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
    }

    private fun setTrackData(track: Track) {
        binding.nameOfSong.text = track.trackName
        binding.authorOfSong.text = track.artistName
        binding.durationMinutes.text = track.trackTime.time
        binding.albumYear.text = track.releaseDate.substring(0, 4)
        binding.albumCountry.text = track.country
        binding.albumName.text = track.collectionName
        binding.albumGenre.text = track.primaryGenreName
    }

    private fun startProgressUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    Log.d("TIME_IN_PLAYER", "${mediaPlayer.currentPosition}")
                    binding.time.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 300L)
                } else {
                    binding.time.text = getString(R.string.zero_time)
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        pauseProgressUpdate()
    }

    private fun pauseProgressUpdate() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun getCoverArtwork(track: Track) =
        track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun initBackButton() {
        val back = findViewById<ImageView>(R.id.back_button)

        back.setOnClickListener {
            finish()
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.Inited
            binding.playButton.isEnabled = true
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.Inited
            binding.playButton.setImageResource(R.drawable.play_button)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.button_play)
        playerState = PlayerState.Playing
        startProgressUpdate()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = PlayerState.Paused
        pauseProgressUpdate()
    }

    private fun switchPlayerState() {
        when (playerState) {
            PlayerState.Playing -> {
                pausePlayer()
            }

            PlayerState.Inited, PlayerState.Paused -> {
                startPlayer()
            }

            PlayerState.NotInited -> throw IllegalStateException("Player can't be in this state")
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
}

sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}