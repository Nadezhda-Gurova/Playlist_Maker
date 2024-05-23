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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
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
                // ничего не делаем
                dialog.dismiss()
            }.setNegativeButton("Завершить") { dialog, which ->
                // сохраняем изменения и выходим
                // save()
                dialog.dismiss()
                navigateBack()

//                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        binding.backButton.setOnClickListener {
            if (binding.name.text.toString().isNotBlank()) {
                confirmDialog.show()
            } else {
//                requireActivity().onBackPressedDispatcher.onBackPressed()
                navigateBack()
            }
        }

        binding.createButton.isEnabled = false
        binding.name.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            activateCreateButton(s)
        })

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    curUri = uri.toString()
                    binding.playlistCover.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        // Обработка нажатия на область для отображения обложки
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
        requireActivity().supportFragmentManager.popBackStack()
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
//        requireActivity().onBackPressedDispatcher.onBackPressed()
        navigateBack()
    }

    private fun activateCreateButton(s: CharSequence?) {
        binding.createButton.isEnabled = !s.isNullOrBlank()
    }

//    private fun saveImageToPrivateStorage(uri: Uri) {
//        //создаём экземпляр класса File, который указывает на нужный каталог
//        val filePath =
//            File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//        //создаем каталог, если он не создан
//        if (!filePath.exists()) {
//            filePath.mkdirs()
//        }
//        //создаём экземпляр класса File, который указывает на файл внутри каталога
//        val file = File(filePath, "first_cover.jpg")
//        // создаём входящий поток байтов из выбранной картинки
//        val inputStream = context?.contentResolver?.openInputStream(uri)
//        // создаём исходящий поток байтов в созданный выше файл
//        val outputStream = FileOutputStream(file)
//        // записываем картинку с помощью BitmapFactory
//        BitmapFactory
//            .decodeStream(inputStream)
//            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
//    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        // Создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        // Создаём каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        // Создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "first_cover.jpg")

        // Создаём входящий поток байтов из выбранной картинки
        val inputStream = context?.contentResolver?.openInputStream(uri)

        // Декодируем поток в Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Проверяем, что Bitmap не равен null
        if (bitmap != null) {
            // Создаём исходящий поток байтов в созданный выше файл
            val outputStream = FileOutputStream(file)

            // Записываем картинку с помощью Bitmap.compress
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            // Закрываем потоки
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
}
