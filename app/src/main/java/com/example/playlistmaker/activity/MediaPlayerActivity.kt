package com.example.playlistmaker.activity

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Handler
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


class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var handler: Handler
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

    private var currentPositionInSeconds = 0
    private val updateIntervalMillis = 1000L

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

        songName.text = track.trackName
        songAuthor.text = track.artistName
        durationMinutes.text = track.trackTime.time
        albumYear.text = track.releaseDate.substring(0, 4)
        albumCountry.text = track.country
        albumName.text = track.collectionName
        genre.text = track.primaryGenreName

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

        var isPlayButtonClicked = false
        playButton.setOnClickListener {
            isPlayButtonClicked = if (!isPlayButtonClicked) {
                playButton.setImageResource(R.drawable.button2)
                drawTrackTime()
                startProgressUpdate()
                true
            } else {
                playButton.setImageResource(R.drawable.play_button)
                pauseProgressUpdate()
                false
            }
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

        handler = Handler(Looper.getMainLooper())

    }

    private fun pauseProgressUpdate() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun startProgressUpdate() {
        handler.postDelayed({
            drawTrackTime()
            startProgressUpdate()
        }, updateIntervalMillis)
    }

    private fun drawTrackTime() {
        val formattedTime = formatTime(currentPositionInSeconds)
        time.text = formattedTime
        currentPositionInSeconds++
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    override fun onDestroy() {
        super.onDestroy()
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
}