package com.example.covid19vaccinationcentermapservice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData

@Database(entities = [VaccinationCenterData::class], version = 2)
abstract class DataBaseVaccinationCenter : RoomDatabase() {
    abstract fun vaccinationCenterDAO() : VaccinationCenterDAO

}