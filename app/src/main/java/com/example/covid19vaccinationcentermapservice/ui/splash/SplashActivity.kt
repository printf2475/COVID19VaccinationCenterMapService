package com.example.covid19vaccinationcentermapservice.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.covid19vaccinationcentermapservice.R
import com.example.covid19vaccinationcentermapservice.databinding.ActivitySplashBinding
import com.example.covid19vaccinationcentermapservice.ui.map.MapActivity
import com.example.covid19vaccinationcentermapservice.util.Define
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        initObserver()




    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vaccinationLoadEvent.collect{
                    when(it){
                        SplashViewModel.VaccinationLoadEvent.Fail ->showToast()
                        else -> {}
                    }
                }
            }
        }

        viewModel.loadingProgress.observe(this){
            if (it >=Define.AppData.PROGRESS_FINISH_COUNT){
                val intent = Intent(this, MapActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }
    }

    private fun showToast(){
        Toast.makeText(this, getString(R.string.error_vaccination_center_load), Toast.LENGTH_LONG).show()
    }
}