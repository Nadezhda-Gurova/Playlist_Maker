package com.example.playlistmaker.sharing.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.sharing.data.EmailData

class ExternalNavigator(
    private val context: Context,
) {
    fun shareLink(shareAppLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
            context.startActivity(Intent.createChooser(this, ""))
        }
    }

    fun openLink(termsLink: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, termsLink)
            type = "text/plain"
            context.startActivity(this)
        }
    }

    fun openEmail(supportEmailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.body)
            context.startActivity(this)
        }
    }
}
