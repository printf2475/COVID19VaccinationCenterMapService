package com.example.covid19vaccinationcentermapservice.data.model

data class VaccinationCenterResult(
    val currentCount: Int,
    val `data`: List<VaccinationCenterData>,
    val matchCount: Int,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)
