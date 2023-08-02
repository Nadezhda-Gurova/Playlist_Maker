package com.example.playlistmaker.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.OnTrackClickListener
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchTrackHistory
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.data.TrackTime
import com.example.playlistmaker.extentions.hideKeyboard
import com.example.playlistmaker.network.IMDbApi
import com.example.playlistmaker.network.ITunesResponse
import com.example.playlistmaker.recyclerview.TrackAdapter
import com.example.playlistmaker.recyclerview.TrackTypeAdapter
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KFunction0

class SearchActivity : AppCompatActivity() {

    private val tracks = arrayListOf<Track>()
    lateinit var trackAdapter: TrackAdapter

    private lateinit var inputEditText: EditText
    private lateinit var nothingFoundImg: ImageView
    private lateinit var nothingFoundText: TextView
    private lateinit var badConnectionImg: ImageView
    private lateinit var badConnectionText: TextView
    private lateinit var badConnectionButton: Button
    private lateinit var rvTrack: RecyclerView
    private lateinit var hintMessage: TextView
    private lateinit var cleanHistoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val sharedPrefs = getSharedPreferences(VIEWED_TRACK, MODE_PRIVATE)
        val searchHistory = SearchTrackHistory.Impl(sharedPrefs)

        val onTrackClickListener = object : OnTrackClickListener {

            override fun onTrackClick(track: Track) {
                searchHistory.addTrack(track)
            }
        }


        val back = findViewById<ImageView>(R.id.back_in_search)
        back.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.inputEditText)
        nothingFoundImg = findViewById(R.id.nothing_found)
        nothingFoundText = findViewById(R.id.nothing_found_text)
        badConnectionImg = findViewById(R.id.bad_connection)
        badConnectionText = findViewById(R.id.bad_connection_text)
        badConnectionButton = findViewById(R.id.bad_connection_button)
        hintMessage = findViewById(R.id.hint_message)
        cleanHistoryButton = findViewById(R.id.clean_history_button)
        rvTrack = findViewById(R.id.recyclerView)

        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        rvTrack.adapter = trackAdapter

        fun loadHistory() {
            hideNothingFound()
            tracks.clear()
            val historyTracks = searchHistory.getTracks()
            tracks.addAll(historyTracks)
            rvTrack.adapter?.notifyDataSetChanged()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            loadHistory()
            clearButton.hideKeyboard()
        }

        cleanHistoryButton.setOnClickListener {
            tracks.clear()
            sharedPrefs.edit().clear().apply()
            rvTrack.adapter?.notifyDataSetChanged()
        }

        val historyTracks = searchHistory.getTracks()

        if (historyTracks.isNotEmpty()) {
            tracks.addAll(historyTracks)
        } else {
            hintMessage.visibility = View.GONE
            cleanHistoryButton.visibility = View.GONE
        }

        val simpleTextWatcher = getSimpleTextWatcher(clearButton, ::loadHistory)
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            val historyVisibility = if (hasFocus && inputEditText.text.isEmpty() && searchHistory.getTracks()
                    .isNotEmpty()
            ) View.VISIBLE else View.GONE
            hintMessage.visibility = historyVisibility
            cleanHistoryButton.visibility = historyVisibility

        }

        setUpRecyclerWithRetrofit()
    }


    private fun setUpRecyclerWithRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(TrackTime::class.java, TrackTypeAdapter())
                        .create()
                )
            )
            .build()


        val iTunesService = retrofit.create(IMDbApi::class.java)

        badConnectionButton.setOnClickListener {
            inputEditText.hideKeyboard()
            searchSong(iTunesService)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(iTunesService)
                true
            } else false
        }
    }

    private fun getSimpleTextWatcher(clearButton: ImageView, loadHistory: KFunction0<Unit>) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isTextEntered = if (
                    inputEditText.hasFocus() && s?.isEmpty() == true
                ) View.VISIBLE else View.GONE

                hintMessage.visibility = isTextEntered
                cleanHistoryButton.visibility = isTextEntered

                clearButton.visibility = clearButtonVisibility(s)

                if (isTextEntered == View.VISIBLE) {
                    loadHistory()
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
            nothingFoundImg.visibility = View.VISIBLE
            nothingFoundText.visibility = View.VISIBLE
            tracks.clear()
            rvTrack.adapter?.notifyDataSetChanged()
        } else {
            view?.hideKeyboard()
            nothingFoundImg.visibility = View.GONE
            nothingFoundText.visibility = View.GONE
        }
    }

    private fun showBadConnection(view: EditText) {
        view.hideKeyboard()
        badConnectionImg.visibility = View.VISIBLE
        badConnectionText.visibility = View.VISIBLE
        badConnectionButton.visibility = View.VISIBLE
        tracks.clear()
        rvTrack.adapter?.notifyDataSetChanged()
    }

    private fun searchSong(
        iTunesService: IMDbApi
    ) {
        iTunesService.search(inputEditText.text.toString()).enqueue(
            object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    if (response.code() == 200) {
                        hideNothingFound()
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            rvTrack.adapter?.notifyDataSetChanged()
                        } else {
                            showNothingFound(
                                inputEditText.text.toString(),
                                inputEditText
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    Log.e("onFailure", t.message, t)
                    hideNothingFound()
                    showBadConnection(inputEditText)
                }

            })
    }

    private fun hideNothingFound() {
        nothingFoundImg.visibility = View.GONE
        nothingFoundText.visibility = View.GONE
        badConnectionImg.visibility = View.GONE
        badConnectionText.visibility = View.GONE
        badConnectionButton.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_INPUT, ""))
    }

    companion object {
        const val VIEWED_TRACK = "VIEWED_TRACK"
        const val VIEWED_TRACKS = "key_for_viewed_tracks"
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

}

