package com.example.playlistmaker.media.ui.playlist_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding: FragmentPlaylistCreationBinding
        get() = _binding!!

    private val viewModel: EditPlaylistViewModel by viewModel()

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

        viewModel.playlistEdit.observe(viewLifecycleOwner) { playlist ->
            // Заполнение полей экрана данными о плейлисте
            binding.name.setText(playlist.name)
            binding.description.setText(playlist.description)

            Glide.with(this)
                .load(playlist.imagePath)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.round_corner)))
                .into(binding.playlistCover)
        }

        // Обработка нажатия на кнопку "Сохранить"
        binding.createButton.setOnClickListener {
            val title = binding.name.text.toString()
            val description = binding.description.text.toString()
            viewModel.editPlaylist(playlistId, title, description, playlistId.toString())
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
