package com.example.playlistmaker.settings.ui

import android.app.UiModeManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.App

class SettingsActivity(
) : ComponentActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.bind(findViewById(R.id.root))

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(this),
                Creator.provideSettingsInteractor(
                    getSystemService(Context.UI_MODE_SERVICE) as UiModeManager,
                    application.getSharedPreferences(App.DARK_THEME_MODE, MODE_PRIVATE)
                )
            )
        )[SettingsViewModel::class.java]

        viewModel.darkThemeLiveData().observe(this) { isChecked ->
            binding.blackTheme.isChecked = isChecked
        }

        initBackButton()

        try {
            initShare()
        } catch (_: ActivityNotFoundException) {
        }


        try {
            initWriteToSupport()
        } catch (_: ActivityNotFoundException) {
        }


        try {
            initTermOfUse()
        } catch (_: ActivityNotFoundException) {
        }

        setBlackTheme()
    }


    private fun setBlackTheme() {
        binding.blackTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateBlackTheme(checked)
        }

    }

    private fun initBackButton() {
        val back = findViewById<ImageView>(R.id.back_button)
        back.setOnClickListener {
            finish()
        }
    }

    private fun initTermOfUse() {
        val termsOfUse = findViewById<View>(R.id.terms_of_use_panel)
        termsOfUse.setOnClickListener {
            viewModel.openTerms()
        }
    }

    private fun initShare() {
        val search = findViewById<View>(R.id.share_text_panel)

        search.setOnClickListener {
            viewModel.shareData()
        }
    }

    private fun initWriteToSupport() {
        val writeToSupport = findViewById<View>(R.id.write_to_support_panel)
        writeToSupport.setOnClickListener {
            viewModel.openSupport()
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}
