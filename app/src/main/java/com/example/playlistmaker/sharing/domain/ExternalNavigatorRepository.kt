package com.example.playlistmaker.sharing.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.EmailData

class ExternalNavigatorRepository(
    private val context: Context,
) {
    fun shareLink() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareAppLink))
            type = "text/plain"
            context.startActivity(Intent.createChooser(this, ""))
        }
    }

    fun openLink() {
        Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.public_offer))).apply {
            context.startActivity(Intent.createChooser(this, context.getString(R.string.title)))
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