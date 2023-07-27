package com.example.playlistmaker.activity

import android.content.SharedPreferences
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
import com.example.playlistmaker.recyclerview.CustomTypeAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.recyclerview.TrackAdapter
import com.example.playlistmaker.data.TrackTime
import com.example.playlistmaker.extentions.hideKeyboard
import com.example.playlistmaker.network.IMDbApi
import com.example.playlistmaker.network.ITunesResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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


        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            clearButton.hideKeyboard()
        }

        val sharedPrefs = getSharedPreferences(VIEWED_TRACK, MODE_PRIVATE)


        val onTrackClickListener = object : OnTrackClickListener {

            override fun onTrackClick(track: Track) {
                var tracksJson = sharedPrefs?.getString(VIEWED_TRACK_KEY, null)

                tracksJson = tracksJson ?: "[]"
                var tracks = createTracksFromJson(tracksJson)

                var index = -1
                for (i in tracks.indices) {
                    if (tracks[i].trackId == track.trackId) {
                        index = i
                    }
                }
                if (index != -1) {
                    tracks.drop(index)
                    tracks += track
                } else {
                    tracks += track
                }
                sharedPrefs.edit().putString(
                    VIEWED_TRACK_KEY, createJsonFromTracks(
                        tracks
                    )
                )
                    .apply()

            }
        }



        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == VIEWED_TRACK_KEY) {
                val curTrack = sharedPreferences?.getString(VIEWED_TRACK_KEY, null)
                if (curTrack != null) {
//                    HistoryAdapter().data.add(0, createTracksFromJson(curTrack))
                    trackAdapter.notifyItemInserted(0)
                }
            }
        }

        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)


        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        rvTrack.adapter = trackAdapter

        val simpleTextWatcher = getSimpleTextWatcher(clearButton)
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            val inFocus = if (hasFocus && inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
            hintMessage.visibility = inFocus
            cleanHistoryButton.visibility = inFocus
        }

        setUpRecyclerWithRetrofit()
    }

    private fun createJsonFromTracks(tracks: Array<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): Array<Track> {
        Log.d("Я ТУТ", json)
        return Gson().fromJson(json, Array<Track>::class.java)
    }


    private fun setUpRecyclerWithRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(TrackTime::class.java, CustomTypeAdapter())
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

    private fun getSimpleTextWatcher(clearButton: ImageView) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isTextEntered = if (
                inputEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            hintMessage.visibility = isTextEntered
            cleanHistoryButton.visibility = isTextEntered

            clearButton.visibility = clearButtonVisibility(s)
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
        const val VIEWED_TRACK_KEY = "key_for_viewed_track"
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

}

