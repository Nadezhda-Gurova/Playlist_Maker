package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.sharing.data.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity(
) : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.bind(findViewById(R.id.root))

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
            openLink()
        }
    }

    private fun initShare() {
        binding.shareTextPanel.setOnClickListener {
            shareLink()
        }
    }

    private fun initWriteToSupport() {
        binding.writeToSupportPanel.setOnClickListener {
           openEmail()
        }
    }

    fun shareLink() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getString(R.string.shareAppLink))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
            startActivitySafely { startActivity(Intent.createChooser(this, "")) }
        }
    }

    fun openLink() {
        Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.public_offer))).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivitySafely {
                startActivity(
                    Intent.createChooser(
                        this,
                        getString(R.string.title)
                    )
                )
            }
        }
    }

    fun openEmail() {
        val supportEmailData: EmailData = getSupportEmailData()
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivitySafely { startActivity(this) }
        }
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = getString(R.string.mail),
            subject = getString(R.string.letter_subject),
            body = getString(R.string.letter_text)
        )
    }

    private fun startActivitySafely(action: () -> Unit) {
        try {
            action()
        } catch (_: ActivityNotFoundException) {
        }
    }
}
