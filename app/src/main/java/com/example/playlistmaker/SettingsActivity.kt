package com.example.playlistmaker

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.back_button)

        back.setOnClickListener {
            finish()
        }

        val search = findViewById<View>(R.id.share_text_panel)

        search.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {

                val shareMessage = getString(R.string.message)
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
                startActivity(createChooser(this, ""))
            }
        }

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

        val termsOfUse = findViewById<View>(R.id.terms_of_use_panel)
        termsOfUse.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.public_offer)))
            startActivity(createChooser(intent, getString(R.string.title)))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}