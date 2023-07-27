package com.example.playlistmaker.activity

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.DARK_THEME_MODE
import com.example.playlistmaker.App.Companion.DARK_THEME_TEXT_KEY
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initBackButton()

        initShare()

        initWriteToSupport()

        initTermOfUse()

        setBlackTheme()
    }

    private fun setBlackTheme() {

        val themeSwitcher = findViewById<SwitchCompat>(R.id.black_theme)

        val sharedPrefs = getSharedPreferences(DARK_THEME_MODE, MODE_PRIVATE)
        themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_TEXT_KEY, false)


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_TEXT_KEY, checked)
                .apply()
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.public_offer)))
            startActivity(createChooser(intent, getString(R.string.title)))
        }
    }

    private fun initShare() {
        val search = findViewById<View>(R.id.share_text_panel)

        search.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {

                val shareMessage = getString(R.string.message)
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
                startActivity(createChooser(this, ""))
            }
        }
    }

    private fun initWriteToSupport() {
        val writeToSupport = findViewById<View>(R.id.write_to_support_panel)
        writeToSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.letter_subject)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.letter_text)
                )
                startActivity(this)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}
