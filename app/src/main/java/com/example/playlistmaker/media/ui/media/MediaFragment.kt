package com.example.playlistmaker.media.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private val mediaViewModel: PlaylistMakerViewModel by activityViewModels()

    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding
        get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediaAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)

            }
        }

        tabMediator.attach()
    }

    override fun onDestroyView() {
        _binding = null
        tabMediator.detach()
        super.onDestroyView()
    }
    
}

