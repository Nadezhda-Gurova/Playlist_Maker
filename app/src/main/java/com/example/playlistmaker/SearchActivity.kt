package com.example.playlistmaker
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

const val log = "# SearchActivity"
class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
    }

    init {
        Log.d(log, "constructor")
    }

    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(log, "onCreate")
        setContentView(R.layout.activity_search)

//        val linearLayout = findViewById<ConstraintLayout>(R.id.container)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById(R.id.inputEditText)

        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // empty
                } else {
                    // empty
                }
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
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(log, "onSaveInstanceState")
        outState.putString(SEARCH_INPUT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(log, "onRestoreInstanceState")
        inputEditText.setText(savedInstanceState.getString(SEARCH_INPUT, ""))
    }

    override fun onResume() {
        super.onResume()
        Log.d(log, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(log, "onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.d(log, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(log, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(log, "onDestroy")
    }

}