package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<ImageView>(R.id.back_in_search)

        back.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)

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

        val rvTrack = findViewById<RecyclerView>(R.id.recyclerView)
        rvTrack.adapter = trackAdapter


        val iTunesBaseUrl = "https://itunes.apple.com"

        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunesService = retrofit.create(IMDbApi::class.java)


        fun showMessage(string: String, s: String) {
            Toast.makeText(applicationContext, "ГАВ", Toast.LENGTH_LONG).show()
        }
//        {
//            if (inputEditText?.text.toString().isNotEmpty()) {
//                placeholderMessage.visibility = View.VISIBLE
//                tracks.clear()
//                rvTrack.adapter?.notifyDataSetChanged()
//                placeholderMessage.text = text
//                if (additionalMessage.isNotEmpty()) {
//                    Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                        .show()
//                }
//            } else {
//                placeholderMessage.visibility = View.GONE
//            }
//        }


        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesService.search(inputEditText?.text.toString()).enqueue(
                    object : Callback<ITunesResponse> {
                        override fun onResponse(
                            call: Call<ITunesResponse>,
                            response: Response<ITunesResponse>
                        ) {
                            if (response.code() == 200) {
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    tracks.addAll(response.body()?.results!!)
                                    rvTrack.adapter?.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()) {
                                    showMessage(getString(R.string.nothing_found), "")
                                } else {
                                    showMessage("", "")
                                }
                            } else {
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    response.code().toString()
                                )
                            }
                        }

                        override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                            showMessage(
                                getString(R.string.something_went_wrong),
                                t.message.toString()
                            )
                        }

                    })
                true
            } else false
        }
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

