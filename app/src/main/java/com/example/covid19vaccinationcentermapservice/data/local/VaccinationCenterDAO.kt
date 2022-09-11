package com.example.covid19vaccinationcentermapservice.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.covid19vaccinationcentermapservice.data.model.VaccinationCenterData

@Dao
interface VaccinationCenterDAO {

    @Query("SELECT * FROM VaccinationCenterData")
    suspend fun getAll(): List<VaccinationCenterData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(vaccinationCenterData: List<VaccinationCenterData>)

    @Query("DELETE FROM VaccinationCenterData")
    suspend fun deleteAll()
}