package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search_button)

        search.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        val settings = findViewById<Button>(R.id.settings_button)

        settings.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        val media = findViewById<Button>(R.id.media_button)

        media.setOnClickListener {
            navigateTo(MediaActivity::class.java)
        }

    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}
