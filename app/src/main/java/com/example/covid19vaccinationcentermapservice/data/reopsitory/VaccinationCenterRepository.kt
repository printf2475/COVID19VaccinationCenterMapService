package com.example.covid19vaccinationcentermapservice.data.reopsitory

import com.example.covid19vaccinationcentermapservice.data.local.DataBaseVaccinationCenter
import com.example.covid19vaccinationcentermapservice.data.local.VaccinationCenterDAO
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData
import com.example.covid19vaccinationcentermapservice.data.remote.VaccinationCenterRemoteDataSource
import javax.inject.Inject

class VaccinationCenterRepository @Inject constructor(
    private val vaccinationCenterRemoteDataSource: VaccinationCenterRemoteDataSource,
    private val dataBaseVaccinationCenter: DataBaseVaccinationCenter
) {

    suspend fun getVaccinationCenterFromRestApi(page : Int) = vaccinationCenterRemoteDataSource.getVaccinationCenter(page)

    suspend fun getAllVaccinationCenterFromDB() = dataBaseVaccinationCenter.vaccinationCenterDAO().getAll()

    suspend fun saveVaccinationCenterList(vaccinationCenterList : List<VaccinationCenterData>) = dataBaseVaccinationCenter.vaccinationCenterDAO().insertData(vaccinationCenterList)

    suspend fun deleteAllVaccinationCenter() = dataBaseVaccinationCenter.vaccinationCenterDAO().deleteAll()

}