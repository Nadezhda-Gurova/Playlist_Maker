package com.example.playlistmaker.media.ui.playlist_edit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding: FragmentPlaylistCreationBinding
        get() = _binding!!

    private val viewModel: EditPlaylistViewModel by viewModel()
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

        // Получение информации о плейлисте из аргументов
        val playlistId = arguments?.getInt("playlistId")
            ?: throw IllegalArgumentException("Playlist ID not provided")
        viewModel.loadPlaylist(playlistId)

        binding.name.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            activateCreateButton(s)
        })

        viewModel.playlistEdit.observe(viewLifecycleOwner) { playlist ->
            // Заполнение полей экрана данными о плейлисте
            binding.createButton.text = getString(R.string.save)
            binding.name.setText(playlist.name)
            binding.description.setText(playlist.description)
            binding.createButton.text = getString(R.string.save)

            Glide.with(this)
                .load(playlist.imagePath)
                .centerCrop()
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.round_corner)))
                .into(binding.playlistCover)
        }

        // Обработка нажатия на кнопку "Сохранить"
        binding.createButton.setOnClickListener {
            val title = binding.name.text.toString()
            val description = binding.description.text.toString()
            viewModel.editPlaylist(playlistId, title, description, curUri)
            findNavController().navigateUp()
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
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
    }

    private fun activateCreateButton(s: CharSequence?) {
        binding.createButton.isEnabled = !s.isNullOrBlank()
    }
}

