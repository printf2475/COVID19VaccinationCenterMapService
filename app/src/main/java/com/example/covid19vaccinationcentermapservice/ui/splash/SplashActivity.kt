package com.example.covid19vaccinationcentermapservice.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.covid19vaccinationcentermapservice.R
import com.example.covid19vaccinationcentermapservice.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel : SplashViewModel by viewModels()
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


    }
}