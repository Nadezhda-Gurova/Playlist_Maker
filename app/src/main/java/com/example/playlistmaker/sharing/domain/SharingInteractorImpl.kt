package com.example.playlistmaker.sharing.domain

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigatorRepository: ExternalNavigatorRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigatorRepository.shareLink()
    }

    override fun openTerms() {
        externalNavigatorRepository.openLink()
    }

    override fun openSupport() {
        externalNavigatorRepository.openEmail(getSupportEmailData())
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.mail),
            subject = context.getString(R.string.letter_subject),
            body = context.getString(R.string.letter_text)
        )
    }
}