package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.sharing.data.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment(
) : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.darkThemeLiveData().observe(viewLifecycleOwner) { isChecked ->
            binding.blackTheme.isChecked = isChecked
        }
        initShare()
        initWriteToSupport()
        initTermOfUse()
        setBlackTheme()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setBlackTheme() {
        binding.blackTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateBlackTheme(checked)
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
