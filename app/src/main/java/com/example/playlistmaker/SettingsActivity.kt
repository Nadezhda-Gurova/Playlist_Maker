package com.example.playlistmaker

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
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

        val search = findViewById<ImageView>(R.id.share_ic)

        search.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                val shareMessage = "https://practicum.yandex.ru/android-developer/"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
                startActivity(createChooser(this, ""))
            }
        }

        val writeToSupport = findViewById<ImageView>(R.id.write_to_support_ic)
        writeToSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("nyud91@gmail.com"))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Сообщение разработчикам и разработчицам приложения Playlist Maker"
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Спасибо разработчикам и разработчицам за крутое приложение!"
                )
                startActivity(this)
            }
        }

        val termsOfUse = findViewById<ImageView>(R.id.terms_of_use_ic)
        termsOfUse.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,  Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(createChooser(intent, "Choose Application"))
        }
    }

}