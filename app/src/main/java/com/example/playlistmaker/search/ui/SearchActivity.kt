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
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.util.VIEWED_TRACK
import com.example.playlistmaker.util.ui.extentions.hideKeyboard
import com.example.playlistmaker.search.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.search.ui.recyclerview.TrackAdapter
import com.example.playlistmaker.util.LoadingState


class SearchActivity : AppCompatActivity() {

    private val tracks = arrayListOf<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private var detailsRunnable: Runnable? = null
    private lateinit var viewModel: SearchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.bind(findViewById(R.id.root))
        val sharedPrefs = getSharedPreferences(VIEWED_TRACK, MODE_PRIVATE)
        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory(
                Creator.provideSearchTrackHistoryUseCase(this),
                Creator.provideGetTracksListInteractor()
            )
        )[SearchViewModel::class.java]

        viewModel.loadingState.observe(this) { loadingState ->
            renderTracks(loadingState)
        }

        val onTrackClickListener = object : OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                viewModel.addTrack(track)
                if (clickDebounce()) {
                    val intent = Intent(this@SearchActivity, SearchActivity::class.java)
                    intent.putExtra(TRACK_MEDIA, track)
                    startActivity(intent)
                }
            }
        }

        binding.backInSearch.setOnClickListener {
            finish()
        }

        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        binding.recyclerView.adapter = trackAdapter

        binding.clearSearch.setOnClickListener {
            viewModel.loadHistory()
            binding.inputEditText.setText("")
            binding.clearSearch.hideKeyboard()
        }

        binding.cleanHistoryButton.setOnClickListener {
            hideYouSearched()
            hideNothingFound()
            trackAdapter.clearTracks()
            viewModel.clearHistory()
        }

        viewModel.loadHistory()

        val simpleTextWatcher = getSimpleTextWatcher(binding.clearSearch)
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

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

    private val searchRunnable = Runnable { searchSong() }
    private val handler = Handler(Looper.getMainLooper())

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun getSimpleTextWatcher(
        clearButton: ImageView,
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
                val isShowHistory = binding.inputEditText.hasFocus() && s.isEmpty()

                if (isShowHistory) {
                    viewModel.loadHistory()
                }

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
            trackAdapter.clearTracks()
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
        trackAdapter.clearTracks()
    }


    private fun searchSong() {
        hideNothingFound()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.searchTrack(binding.inputEditText.text.toString())
    }

    private fun renderTracks(loadingState: LoadingState<State>) {
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
                    val isTracksEmpty = loadingState.data.tracks.isEmpty()
                    if (loadingState.data.isHistory) {
                        if (isTracksEmpty) {
                            trackAdapter.clearTracks()
                            hideYouSearched()
                        } else {
                            replaceTracks(loadingState)
                        }
                    } else {
                        if (isTracksEmpty) {
                            trackAdapter.clearTracks()
                            showNothingFound(
                                binding.inputEditText.text.toString(),
                                binding.inputEditText
                            )
                        } else {
                            replaceTracks(loadingState)
                        }
                    }
                }
            }
        }
        detailsRunnable = newDetailsRunnable
        handler.post(newDetailsRunnable)
    }

    private fun replaceTracks(loadingState: LoadingState.Success<State>) {
        trackAdapter.replaceTracks(loadingState.data.tracks)
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

