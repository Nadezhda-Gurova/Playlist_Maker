package com.example.playlistmaker.media.ui.playlist_maker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.player.ui.PreviousFragmentCallBack
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistMakerFragment : Fragment() {
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding: FragmentPlaylistCreationBinding
        get() = _binding!!

    private val viewModel: PlaylistMakerViewModel by viewModel()
    private var curUri = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }.setNegativeButton("Завершить") { dialog, which ->
                dialog.dismiss()
                navigateBack()
            }

        binding.backButton.setOnClickListener {
            if (binding.name.text.toString().isNotBlank()) {
                confirmDialog.show()
            } else {
                navigateBack()
            }
        }

        binding.createButton.isEnabled = false
        binding.name.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            activateCreateButton(s)
        })

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    curUri = uri.toString()
                    binding.playlistCover.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            viewModel.createPlaylist(
                name = binding.name.text.toString(),
                description = binding.description.text.toString(),
                imagePath = curUri
            )
            createPlaylistAndNavigateBack()

        }
    }

    private fun navigateBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun createPlaylistAndNavigateBack() {
        val playlistName = binding.name.text.toString()
        val parent = parentFragment
        if (parent is OnPlaylistCreatedListener) {
            parent.onPlaylistCreated(playlistName)
        } else {
            Log.e(
                "PlaylistMakerFragment",
                "Parent fragment is not implementing OnPlaylistCreatedListener"
            )
        }

        Toast.makeText(
            requireContext(),
            "Плейлист $playlistName создан",
            Toast.LENGTH_SHORT
        ).show()
        navigateBack()
    }

    private fun activateCreateButton(s: CharSequence?) {
        binding.createButton.isEnabled = !s.isNullOrBlank()
    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "first_cover.jpg")
        val inputStream = context?.contentResolver?.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap != null) {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            outputStream.close()
            inputStream?.close()
        } else {
            Log.e("saveImageToPrivateStorage", "Failed to decode Bitmap from Uri")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val callback = requireActivity()
        if (callback is PreviousFragmentCallBack) {
            callback.onPreviousFragmentCreation()
        }
    }
}
