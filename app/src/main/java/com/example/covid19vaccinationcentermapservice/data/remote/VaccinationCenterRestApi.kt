package com.example.covid19vaccinationcentermapservice.data.remote

import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterResult
import com.example.covid19vaccinationcentermapservice.BuildConfig
import com.example.covid19vaccinationcentermapservice.util.Define
import retrofit2.http.GET
import retrofit2.http.Query

interface VaccinationCenterRestApi {

    @GET("15077586/v1/centers")
    suspend fun getVaccinationCenter(@Query("page") page: Int,
                                     @Query("perPage") perPage: Int = Define.AppData.PER_PAGE,
                                     @Query("serviceKey") serviceKey: String = BuildConfig.VACCINATION_CENTER_API_KEY): VaccinationCenterResult
}