package com.example.playlistmaker.activity

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.activity.SearchActivity.Companion.TRACK_MEDIA
import com.example.playlistmaker.data.Track
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var albumCover: ShapeableImageView
    private lateinit var songName: TextView
    private lateinit var songAuthor: TextView
    private lateinit var albumYear: TextView
    private lateinit var albumCountry: TextView
    private lateinit var album: TextView
    private lateinit var albumName: TextView
    private lateinit var addToPlaylist: ImageButton
    private lateinit var durationMinutes: TextView
    private lateinit var playButton: ImageButton
    private lateinit var addToFavorites: ImageButton
    private lateinit var time: TextView
    private lateinit var genre: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        initBackButton()

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_MEDIA, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_MEDIA) as? Track
        }

        requireNotNull(track) { "No track provided" }

        findWidgetById()

        setTrackData(track)

        if (albumName.text != null) {
            album.visibility = View.VISIBLE
            albumName.visibility = View.VISIBLE
        }

        Glide.with(this)
            .load(getCoverArtwork(track))
            .centerCrop()
            .placeholder(R.drawable.placeholder_album)
            .into(albumCover)

        var isAddToPlaylistClicked = false

        addToPlaylist.setOnClickListener {
            isAddToPlaylistClicked = if (!isAddToPlaylistClicked) {
                addToPlaylist.setImageResource(R.drawable.button1)
                true
            } else {
                addToPlaylist.setImageResource(R.drawable.add_to_playlist)
                false
            }
        }

        val url = track.previewUrl
        preparePlayer(url)

        playButton.setOnClickListener {
            setUpPlayerState()
        }

        var isAddToFavoritesClicked = false
        addToFavorites.setOnClickListener {
            isAddToFavoritesClicked = if (!isAddToFavoritesClicked) {
                addToFavorites.setImageResource(R.drawable.button3)
                true
            } else {
                addToFavorites.setImageResource(R.drawable.add_to_favorites)
                false
            }
        }
    }

    private fun setTrackData(track: Track) {
        songName.text = track.trackName
        songAuthor.text = track.artistName
        durationMinutes.text = track.trackTime.time
        albumYear.text = track.releaseDate.substring(0, 4)
        albumCountry.text = track.country
        albumName.text = track.collectionName
        genre.text = track.primaryGenreName
    }

    private fun findWidgetById() {
        albumCover = findViewById(R.id.album_cover)
        songName = findViewById(R.id.name_of_song)
        songAuthor = findViewById(R.id.author_of_song)
        durationMinutes = findViewById(R.id.duration_minutes)
        albumYear = findViewById(R.id.album_year)
        albumCountry = findViewById(R.id.album_country)
        album = findViewById(R.id.album)
        albumName = findViewById(R.id.album_name)
        addToPlaylist = findViewById(R.id.add_to_playlist)
        playButton = findViewById(R.id.play_button)
        addToFavorites = findViewById(R.id.add_to_favorites)
        genre = findViewById(R.id.album_genre)
        time = findViewById(R.id.time)
    }

    private val handler = Handler(Looper.getMainLooper())


    private fun startProgressUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                Log.d("TIME_IN_PLAYER", "${mediaPlayer.currentPosition}")
                time.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, 300L)
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

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.play_button)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.button2)
        playerState = STATE_PLAYING
        startProgressUpdate()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        pauseProgressUpdate()
    }

    private fun setUpPlayerState() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

}