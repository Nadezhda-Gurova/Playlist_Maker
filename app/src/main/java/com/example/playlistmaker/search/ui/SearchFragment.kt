package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.MediaPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.ui.extentions.hideKeyboard
import com.example.playlistmaker.search.ui.recyclerview.OnTrackClickListener
import com.example.playlistmaker.search.ui.recyclerview.TrackAdapter
import com.example.playlistmaker.util.LoadingState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val tracks = arrayListOf<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var clickDebounce: (Track) -> Unit
    private lateinit var searchDebounce: (Unit) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchDebounce = debounce(
            SEARCH_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) {
            searchWithDebounce()
        }

        clickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val intent = Intent(requireContext(), MediaPlayerActivity::class.java)
            intent.putExtra(TRACK_MEDIA, track)
            startActivity(intent)
        }

        viewModel.loadingState.observe(viewLifecycleOwner) { loadingState ->
            renderTracks(loadingState)
        }

        val onTrackClickListener = OnTrackClickListener { track ->
            viewModel.addTrack(track)
            clickDebounce(track)
        }

        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        binding.recyclerView.adapter = trackAdapter

        binding.clearSearch.setOnClickListener {
            binding.search.setText("")
            binding.clearSearch.hideKeyboard()
        }

        binding.cleanHistoryButton.setOnClickListener {
            hideYouSearched()
            hideNothingFound()
            trackAdapter.clearTracks()
            viewModel.clearHistory()
        }

        viewModel.searchTrack("")

        binding.search.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            val empty = s.isNullOrEmpty()
            val isShowHistory = binding.search.hasFocus() && empty

            if (isShowHistory) {
                viewModel.searchTrack("")
            }

            binding.clearSearch.isVisible = !empty
            if (!empty) {
                searchWithDebounce()
            }
        })

        binding.badConnectionButton.setOnClickListener {
            hideYouSearched()
            binding.search.hideKeyboard()
            searchWithDebounce()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun searchWithDebounce() {
        trackAdapter.clearTracks()
        hideYouSearched()
        hideNothingFound()
        binding.loader.isVisible = true
        viewModel.searchTrack(binding.search.text.toString())
    }

    private fun showNothingFound(text: String, view: EditText?) {
        if (text.isEmpty()) {
            view?.hideKeyboard()
            hideYouSearched()
        } else {
            view?.hideKeyboard()
            binding.nothingFound.isVisible = true
            binding.nothingFoundText.isVisible = true
            trackAdapter.clearTracks()
        }
    }

    private fun showBadConnection(view: EditText) {
        hideYouSearched()
        view.hideKeyboard()
        binding.badConnection.isVisible = true
        binding.badConnectionText.isVisible = true
        binding.badConnectionButton.isVisible = true
        trackAdapter.clearTracks()
    }

    private fun renderTracks(loadingState: LoadingState<State>) {
        binding.loader.isVisible = false
        when (loadingState) {
            is LoadingState.Error -> {
                hideNothingFound()
                showBadConnection(binding.search)
            }

            is LoadingState.Success -> {
                val isTracksEmpty = loadingState.data.tracks.isEmpty()
                if (loadingState.data.isHistory) {
                    hideNothingFound()
                    if (isTracksEmpty) {
                        trackAdapter.clearTracks()
                        hideYouSearched()
                    } else {
                        showYouSearched()
                        replaceTracks(loadingState)
                    }
                } else {
                    if (isTracksEmpty) {
                        trackAdapter.clearTracks()
                        showNothingFound(
                            binding.search.text.toString(),
                            binding.search
                        )
                    } else {
                        hideNothingFound()
                        hideYouSearched()
                        replaceTracks(loadingState)
                    }
                }
            }
        }
    }

    private fun replaceTracks(loadingState: LoadingState.Success<State>) {
        trackAdapter.replaceTracks(loadingState.data.tracks)
    }

    private fun hideYouSearched() {
        changeYouSearchedVisibility(View.GONE)
    }

    private fun showYouSearched() {
        changeYouSearchedVisibility(View.VISIBLE)
    }

    private fun changeYouSearchedVisibility(visibility: Int) {
        binding.youSearched.visibility = visibility
        binding.cleanHistoryButton.visibility = visibility
    }

    private fun hideNothingFound() {
        binding.nothingFound.isVisible = false
        binding.nothingFoundText.isVisible = false
        binding.badConnection.isVisible = false
        binding.badConnectionText.isVisible = false
        binding.badConnectionButton.isVisible = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, binding.search.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.search.setText(savedInstanceState.getString(SEARCH_INPUT, ""))
        }
    }

    companion object {
        const val TRACK_MEDIA = "track_media"
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}