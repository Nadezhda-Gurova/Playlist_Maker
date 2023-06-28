package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_INPUT = "SEARCH_INPUT"
    }

    private var inputEditText: EditText? = null

    private val tracks = arrayListOf<Track>()

    private val trackAdapter = TrackAdapter(tracks)
    private var nothingFoundImg: ImageView? = null
    private var nothingFoundText: TextView? = null
    private var badConnectionImg: ImageView? = null
    private var badConnectionText: TextView? = null
    private var badConnectionButton: Button? = null
    private var rvTrack: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<ImageView>(R.id.back_in_search)

        back.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)
        nothingFoundImg = findViewById(R.id.nothing_found)
        nothingFoundText = findViewById(R.id.nothing_found_text)
        badConnectionImg = findViewById(R.id.bad_connection)
        badConnectionText = findViewById(R.id.bad_connection_text)
        badConnectionButton = findViewById(R.id.bad_connection_button)


        clearButton.setOnClickListener {
            inputEditText?.setText("")
            tracks.clear()
            clearButton.hideKeyboard()
        }

        val simpleTextWatcher = object : TextWatcher {
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
        inputEditText?.addTextChangedListener(simpleTextWatcher)

        rvTrack = findViewById(R.id.recyclerView)
        rvTrack?.adapter = trackAdapter


        val iTunesBaseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(TrackTime::class.java, CustomTypeAdapter())
                        .create()
                )
            )
            .build()


        val iTunesService = retrofit.create(IMDbApi::class.java)

        badConnectionButton?.setOnClickListener {
            inputEditText!!.hideKeyboard()
            searchSong(iTunesService, rvTrack!!)
        }

        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(iTunesService, rvTrack!!)
                true
            } else false
        }
    }

    private fun showNothingFound(text: String, view: EditText?) {
        if (text.isNotEmpty()) {
            view?.hideKeyboard()
            nothingFoundImg?.visibility = View.VISIBLE
            nothingFoundText?.visibility = View.VISIBLE
            tracks.clear()
            rvTrack?.adapter?.notifyDataSetChanged()
        } else {
            view?.hideKeyboard()
            nothingFoundImg?.visibility = View.GONE
            nothingFoundText?.visibility = View.GONE
        }
    }

    private fun showBadConnection(view: EditText) {
        view.hideKeyboard()
        badConnectionImg?.visibility = View.VISIBLE
        badConnectionText?.visibility = View.VISIBLE
        badConnectionButton?.visibility = View.VISIBLE
        tracks.clear()
        rvTrack?.adapter?.notifyDataSetChanged()
    }

    private fun searchSong(
        iTunesService: IMDbApi,
        rvTrack: RecyclerView
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
        nothingFoundImg?.visibility = View.GONE
        nothingFoundText?.visibility = View.GONE
        badConnectionImg?.visibility = View.GONE
        badConnectionText?.visibility = View.GONE
        badConnectionButton?.visibility = View.GONE
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

}


interface IMDbApi {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}


class ITunesResponse(
    val resultCount: Int,
    val results: List<Track>,
)

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!inputMethodManager.isActive) return
    inputMethodManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.RESULT_UNCHANGED_SHOWN
    )
}

