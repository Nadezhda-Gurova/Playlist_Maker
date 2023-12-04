package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.bind(findViewById(R.id.root))

        binding.searchButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        binding.settingsButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        binding.mediaButton.setOnClickListener {
            navigateTo(MediaActivity::class.java)
        }
    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}
