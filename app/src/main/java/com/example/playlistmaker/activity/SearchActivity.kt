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
import com.example.playlistmaker.recyclerview.CustomTypeAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.recyclerview.TrackAdapter
import com.example.playlistmaker.data.TrackTime
import com.example.playlistmaker.extentions.hideKeyboard
import com.example.playlistmaker.network.IMDbApi
import com.example.playlistmaker.network.ITunesResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var inputEditText: EditText? = null
    private val tracks = arrayListOf<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private lateinit var nothingFoundImg: ImageView
    private lateinit var nothingFoundText: TextView
    private lateinit var badConnectionImg: ImageView
    private lateinit var badConnectionText: TextView
    private lateinit var badConnectionButton: Button
    private lateinit var rvTrack: RecyclerView


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


        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputEditText?.setText("")
            tracks.clear()
            clearButton.hideKeyboard()
        }

        val simpleTextWatcher = getSimpleTextWatcher(clearButton)
        inputEditText?.addTextChangedListener(simpleTextWatcher)

        setUpRecyclerWithRetrofit()
    }

    private fun setUpRecyclerWithRetrofit() {
        rvTrack = findViewById(R.id.recyclerView)
        rvTrack.adapter = trackAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNESBASEURL)
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
            inputEditText!!.hideKeyboard()
            searchSong(iTunesService)
        }

        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
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
        iTunesService.search(inputEditText?.text.toString()).enqueue(
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
                                inputEditText?.text.toString(),
                                inputEditText
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    Log.e("onFailure", t.message, t)
                    hideNothingFound()
                    showBadConnection(inputEditText!!)
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
        outState.putString(SEARCH_INPUT, inputEditText?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText?.setText(savedInstanceState.getString(SEARCH_INPUT, ""))
    }

    override fun onDestroy() {
        super.onDestroy()
        inputEditText = null
    }

    companion object {
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val ITUNESBASEURL = "https://itunes.apple.com"
    }

}

