package com.example.playlistmaker.settings.ui

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity(
) : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.bind(findViewById(R.id.root))

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(),
                Creator.provideSettingsInteractor(
                    getSystemService(Context.UI_MODE_SERVICE) as UiModeManager,
                    Creator.provideDarkModeRepository()
                )
            )
        )[SettingsViewModel::class.java]

        viewModel.darkThemeLiveData().observe(this) { isChecked ->
            binding.blackTheme.isChecked = isChecked
        }

        initBackButton()
        initShare()
        initWriteToSupport()
        initTermOfUse()
        setBlackTheme()
    }

    private fun setBlackTheme() {
        binding.blackTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateBlackTheme(checked)
        }
    }

    private fun initBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun initTermOfUse() {
        binding.termsOfUsePanel.setOnClickListener {
            viewModel.openTerms()
        }
    }

    private fun initShare() {
        binding.shareTextPanel.setOnClickListener {
            viewModel.shareData()
        }
    }

    private fun initWriteToSupport() {
        binding.writeToSupportPanel.setOnClickListener {
            viewModel.openSupport()
        }
    }
}
