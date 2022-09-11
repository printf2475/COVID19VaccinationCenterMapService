package com.example.covid19vaccinationcentermapservice.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.covid19vaccinationcentermapservice.R
import com.example.covid19vaccinationcentermapservice.databinding.ActivityMainBinding
import com.example.covid19vaccinationcentermapservice.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val viewModel : MapViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}