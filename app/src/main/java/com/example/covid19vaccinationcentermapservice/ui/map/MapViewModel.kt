package com.example.covid19vaccinationcentermapservice.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData
import com.example.covid19vaccinationcentermapservice.data.reopsitory.VaccinationCenterRepository
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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


    init {
        getAllVaccinationCenterFromDB()
    }


    fun getAllVaccinationCenterFromDB() = viewModelScope.launch {
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
}