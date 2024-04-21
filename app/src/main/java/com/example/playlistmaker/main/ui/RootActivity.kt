package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.detailsFragment, R.id.moviesCastFragment -> {
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                else -> {
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("###" + javaClass.name, "onStart")
    }

    override fun onResume() {
        Log.d("###" + javaClass.name, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("###" + javaClass.name, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("###" + javaClass.name, "onStop")
        super.onStop()
    }


}