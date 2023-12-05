package com.example.playlistmaker.sharing.domain

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.mail)//todo
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.mail),
            subject = context.getString(R.string.mail),//todo
            body = context.getString(R.string.mail)//todo
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.mail)//todo
    }
}