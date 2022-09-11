package com.example.covid19vaccinationcentermapservice.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19vaccinationcentermapservice.data.reopsitory.VaccinationCenterRepository
import com.example.covid19vaccinationcentermapservice.util.Define
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val vaccinationCenterRepository: VaccinationCenterRepository
) : ViewModel() {

    private val _vaccinationLoadEvent = MutableLiveData<VaccinationLoadEvent>()
    val vaccinationLoadEvent : LiveData<VaccinationLoadEvent> get() = _vaccinationLoadEvent





    sealed class VaccinationLoadEvent {
        object Loading : VaccinationLoadEvent()
        object Success : VaccinationLoadEvent()
        object Fail : VaccinationLoadEvent()
    }
}