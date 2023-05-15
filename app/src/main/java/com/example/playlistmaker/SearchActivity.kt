package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_INPUT = "SEARCH_INPUT"
    }

    private var inputEditText: EditText? = null

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
            Thread {
                inputEditText?.setText("")
            }.start()
            inputEditText?.setText("")
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

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!inputMethodManager.isActive) return
    inputMethodManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.RESULT_UNCHANGED_SHOWN
    )
}