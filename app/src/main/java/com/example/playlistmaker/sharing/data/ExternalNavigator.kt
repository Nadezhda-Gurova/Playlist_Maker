package com.example.playlistmaker.sharing.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R

class ExternalNavigator(
    private val context: Context,
) {
    fun shareLink() {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareAppLink))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
            startActivitySafely { context.startActivity(Intent.createChooser(this, "")) }
        }
    }

    fun openLink() {
        Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.public_offer))).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivitySafely {
                context.startActivity(
                    Intent.createChooser(
                        this,
                        context.getString(R.string.title)
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
            startActivitySafely { context.startActivity(this) }
        }
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.mail),
            subject = context.getString(R.string.letter_subject),
            body = context.getString(R.string.letter_text)
        )
    }

    private fun startActivitySafely(action: () -> Unit) {
        try {
            action()
        } catch (_: ActivityNotFoundException) {
        }
    }
}