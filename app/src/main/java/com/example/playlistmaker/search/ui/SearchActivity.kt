package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.use_case.SearchTrackHistoryUseCase
import com.example.playlistmaker.player.ui.MediaPlayerActivity
import com.example.playlistmaker.util.ui.extentions.hideKeyboard
import com.example.playlistmaker.search.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.search.ui.recyclerview.TrackAdapter
import com.example.playlistmaker.util.LoadingState
import kotlin.reflect.KFunction1


class SearchActivity : AppCompatActivity() {

    private val tracks = arrayListOf<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private val getProductListUseCase = Creator.provideGetTracksListUseCase()
    private var detailsRunnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.bind(findViewById(R.id.root))
        val searchHistory = Creator.provideSearchTrackHistoryUseCase(this)
        
        val onTrackClickListener = object : OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                searchHistory.addTrack(track)
                if (clickDebounce()) {
                    val intent = Intent(this@SearchActivity, MediaPlayerActivity::class.java)
                    intent.putExtra(TRACK_MEDIA, track)
                    startActivity(intent)
                }
            }
        }

        val back = findViewById<ImageView>(R.id.back_in_search)
        back.setOnClickListener {
            finish()
        }

        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        binding.recyclerView.adapter = trackAdapter

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            loadHistory(searchHistory)
            clearButton.hideKeyboard()
        }

        binding.cleanHistoryButton.setOnClickListener {
            hideYouSearched()
            hideNothingFound()
            clearTracks()
            searchHistory.clear()
        }

        loadHistory(searchHistory)

        val simpleTextWatcher = getSimpleTextWatcher(clearButton, searchHistory, ::loadHistory)
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            val historyVisibility =
                if (hasFocus && binding.inputEditText.text.isEmpty() && searchHistory.getTracks()
                        .isNotEmpty()
                ) View.VISIBLE else View.GONE
            binding.hintMessage.visibility = historyVisibility
            binding.cleanHistoryButton.visibility = historyVisibility
        }

        binding.badConnectionButton.setOnClickListener {
            hideYouSearched()
            binding.inputEditText.hideKeyboard()
            searchSong()
        }
    }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun loadHistory(searchHistory: SearchTrackHistoryUseCase) {
        val historyTracks = searchHistory.getTracks()
        if (historyTracks.isNotEmpty()) {
            replaceTracks(historyTracks)
        } else {
            hideYouSearched()
        }
    }

    private val searchRunnable = Runnable { searchSong() }
    private val handler = Handler(Looper.getMainLooper())

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun getSimpleTextWatcher(
        clearButton: ImageView,
        searchHistory: SearchTrackHistoryUseCase,
        loadHistory: KFunction1<SearchTrackHistoryUseCase, Unit>
    ) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                val isVisible = searchHistory.getTracks().isNotEmpty() &&
                        binding.inputEditText.hasFocus() && s.isEmpty()

                val isTextEntered = if (isVisible) View.VISIBLE else View.GONE

                if (isVisible) {
                    loadHistory(searchHistory)
                } else {
                    clearTracks()
                }

                changeYouSearchedVisibility(isTextEntered)

                clearButton.visibility = clearButtonVisibility(s)
                if (s.isNotEmpty()) {
                    searchDebounce()
                }
            }

            private fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

    private fun showNothingFound(text: String, view: EditText?) {
        if (text.isNotEmpty()) {
            view?.hideKeyboard()
            binding.nothingFound.visibility = View.VISIBLE
            binding.nothingFoundText.visibility = View.VISIBLE
            clearTracks()
        } else {
            view?.hideKeyboard()
            hideYouSearched()
        }
    }

    private fun showBadConnection(view: EditText) {
        hideYouSearched()
        view.hideKeyboard()
        binding.badConnection.visibility = View.VISIBLE
        binding.badConnectionText.visibility = View.VISIBLE
        binding.badConnectionButton.visibility = View.VISIBLE
        clearTracks()
    }


    private fun searchSong() {
        hideNothingFound()
        binding.progressBar.visibility = View.VISIBLE
        getProductListUseCase.execute(
            binding.inputEditText.text.toString()
        ) { data -> consumeTrack(data) }
    }

    private fun consumeTrack(loadingState: LoadingState<List<Track>>) {
        val currentRunnable = detailsRunnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }

        val newDetailsRunnable = Runnable {
            binding.progressBar.visibility = View.GONE
            when (loadingState) {
                is LoadingState.Error -> {
                    hideNothingFound()
                    showBadConnection(binding.inputEditText)
                }

                is LoadingState.Success -> {
                    if (loadingState.data.isNotEmpty()) {
                        replaceTracks(loadingState.data)
                    } else {
                        clearTracks()
                        showNothingFound(
                            binding.inputEditText.text.toString(),
                            binding.inputEditText
                        )
                    }
                }
            }
        }
        detailsRunnable = newDetailsRunnable
        handler.post(newDetailsRunnable)
    }


    private fun clearTracks() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun replaceTracks(addedTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(addedTracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideYouSearched() {
        changeYouSearchedVisibility(View.GONE)
    }

    private fun changeYouSearchedVisibility(visibility: Int) {
        binding.hintMessage.visibility = visibility
        binding.cleanHistoryButton.visibility = visibility
    }

    private fun hideNothingFound() {
        binding.nothingFound.visibility = View.GONE
        binding.nothingFoundText.visibility = View.GONE
        binding.badConnection.visibility = View.GONE
        binding.badConnectionText.visibility = View.GONE
        binding.badConnectionButton.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, binding.inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputEditText.setText(savedInstanceState.getString(SEARCH_INPUT, ""))
    }

    companion object {
        const val TRACK_MEDIA = "track_media"
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

