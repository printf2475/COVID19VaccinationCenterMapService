package com.example.covid19vaccinationcentermapservice.data.remote

import javax.inject.Inject

class VaccinationCenterRemoteDataSource@Inject constructor(
    private val restApi : VaccinationCenterRestApi
) {

    suspend fun getVaccinationCenter(page : Int) = restApi.getVaccinationCenter(page = page)
}