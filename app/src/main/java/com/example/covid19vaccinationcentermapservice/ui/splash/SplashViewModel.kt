package com.example.covid19vaccinationcentermapservice.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData
import com.example.covid19vaccinationcentermapservice.data.reopsitory.VaccinationCenterRepository
import com.example.covid19vaccinationcentermapservice.util.Define
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val vaccinationCenterRepository: VaccinationCenterRepository,
) : ViewModel() {

    private val _vaccinationLoadEvent =
        MutableStateFlow<VaccinationLoadEvent>(VaccinationLoadEvent.Loading)
    val vaccinationLoadEvent: StateFlow<VaccinationLoadEvent> get() = _vaccinationLoadEvent

    private val _loadingProgress = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> get() = _loadingProgress

    //coroutineExceptionHandler을 선언한 하위 코루틴에서 Exception 핸들링
    private var coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is Exception -> {
                    viewModelScope.launch {
                        _vaccinationLoadEvent.emit(VaccinationLoadEvent.Fail)
                        Log.e("Exeption", throwable.message.toString())
                    }
                }
            }
        }

    init {
        _loadingProgress.value = 0
        startProgressAndGetVaccinationCenter()
    }


    private fun startProgressAndGetVaccinationCenter() =
        viewModelScope.launch(coroutineExceptionHandler) {
            launch { startProgress() }



            launch {
                getVaccinationCenterFromRestApi().collect {
                    when (it) {
                        VaccinationLoadEvent.Loading -> _vaccinationLoadEvent.emit(it)
                        VaccinationLoadEvent.Success -> _vaccinationLoadEvent.emit(it)
                        VaccinationLoadEvent.Fail -> _vaccinationLoadEvent.emit(it)
                        is VaccinationLoadEvent.LoadData ->
                            vaccinationCenterRepository.saveVaccinationCenterList(it.vaccinationCenterList)
                    }
                }
            }
        }

    private suspend fun startProgress() {
        progressBarMoveToPercent(80)

        when (_vaccinationLoadEvent.value) {
            VaccinationLoadEvent.Loading -> observeVaccinationLoadSuccess()
            VaccinationLoadEvent.Success -> progressBarMoveToPercent(100)
            else -> _vaccinationLoadEvent.emit(VaccinationLoadEvent.Fail)
        }
    }

    private suspend fun observeVaccinationLoadSuccess() {
        vaccinationLoadEvent.collect {
            if (VaccinationLoadEvent.Success == it) {
                progressBarMoveToPercent(100)
            }
        }
    }


    private suspend fun progressBarMoveToPercent(percent: Int) {
        viewModelScope.launch {
            while (loadingProgress.value!! <= percent) {
                _loadingProgress.value = _loadingProgress.value!! + 1
                delay(Define.AppData.PROGRESS_DELAY)
            }
        }
    }


    private suspend fun getVaccinationCenterFromRestApi() = flow {
        vaccinationCenterRepository.deleteAllVaccinationCenter()
        emit(VaccinationLoadEvent.Loading)
        for (page in 1..Define.AppData.LOAD_PAGE_COUNT) {

            val vaccinationCenterData =
                vaccinationCenterRepository.getVaccinationCenterFromRestApi(page)
            emit(VaccinationLoadEvent.LoadData(vaccinationCenterData.data))
        }

        emit(VaccinationLoadEvent.Success)

    }


    sealed class VaccinationLoadEvent {
        object Loading : VaccinationLoadEvent()
        object Success : VaccinationLoadEvent()
        object Fail : VaccinationLoadEvent()
        data class LoadData(val vaccinationCenterList: List<VaccinationCenterData>) :
            VaccinationLoadEvent()
    }
}