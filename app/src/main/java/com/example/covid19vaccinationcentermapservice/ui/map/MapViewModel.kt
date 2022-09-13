package com.example.covid19vaccinationcentermapservice.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData
import com.example.covid19vaccinationcentermapservice.data.reopsitory.VaccinationCenterRepository
import com.example.covid19vaccinationcentermapservice.ui.splash.SplashViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val vaccinationCenterRepository: VaccinationCenterRepository
) : ViewModel() {

    private val _selectedMarker = MutableLiveData<VaccinationCenterData>()
    val selectedMarker: LiveData<VaccinationCenterData> get() = _selectedMarker

    private val markerList = mutableListOf<Marker>()

    private val _vaccinationCenterList = MutableLiveData<List<VaccinationCenterData>>()
    val vaccinationCenterList : LiveData<List<VaccinationCenterData>> get() = _vaccinationCenterList

    private var myLatLng: LatLng? = null

    private val _vaccinationLoadEvent =
        MutableStateFlow<SplashViewModel.VaccinationLoadEvent>(SplashViewModel.VaccinationLoadEvent.Loading)
    val vaccinationLoadEvent: StateFlow<SplashViewModel.VaccinationLoadEvent> get() = _vaccinationLoadEvent


    //coroutineExceptionHandler을 선언한 하위 코루틴에서 Exception 핸들링
    private var coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is Exception -> {
                    viewModelScope.launch {
                        _vaccinationLoadEvent.emit(SplashViewModel.VaccinationLoadEvent.Fail)
                        Log.e("Exeption", throwable.message.toString())
                    }
                }
            }
        }

    init {
        getAllVaccinationCenterFromDB()
    }


    fun getAllVaccinationCenterFromDB() = viewModelScope.launch(coroutineExceptionHandler) {
        vaccinationCenterRepository.getAllVaccinationCenterFromDB().let {
            _vaccinationCenterList.postValue(it)
        }
    }




    fun addMarker(marker: Marker) = markerList.add(marker)

    fun onClickMarker(marker: Marker) {
        marker.tag.let {
            if (it is VaccinationCenterData)
                if (selectedMarker.value == it) _selectedMarker.value = null
                else _selectedMarker.value = it
        }
    }

    fun selectedMarkerClear() {
        _selectedMarker.value = null
    }


    fun setMyLatLng(latLng : LatLng){
        this.myLatLng = latLng
    }

    fun getMyLatLng() = myLatLng

}