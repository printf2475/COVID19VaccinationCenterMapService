package com.example.covid19vaccinationcentermapservice.di

import android.app.Application
import androidx.room.Room
import com.example.covid19vaccinationcentermapservice.data.local.DataBaseVaccinationCenter
import com.example.covid19vaccinationcentermapservice.data.local.VaccinationCenterDAO
import com.example.covid19vaccinationcentermapservice.data.remote.VaccinationCenterRemoteDataSource
import com.example.covid19vaccinationcentermapservice.data.remote.VaccinationCenterRestApi
import com.example.covid19vaccinationcentermapservice.data.reopsitory.VaccinationCenterRepository
import com.example.covid19vaccinationcentermapservice.util.Define
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {




    @Singleton
    @Provides
    fun provideRoom(application: Application): DataBaseVaccinationCenter {
        return  Room.databaseBuilder(
            application,
            DataBaseVaccinationCenter::class.java,
            Define.AppData.DATABASE_NAME
        ).build()
    }


    @Singleton
    @Provides
    fun provideVaccinationCenterRemoteDataSource(
        vaccinationCenterRestApi: VaccinationCenterRestApi
    ) : VaccinationCenterRemoteDataSource {
        return VaccinationCenterRemoteDataSource(vaccinationCenterRestApi)
    }

    @Singleton
    @Provides
    fun provideVaccinationCenterRepository(
        vaccinationCenterRemoteDataSource: VaccinationCenterRemoteDataSource,
        dataBaseVaccinationCenter: DataBaseVaccinationCenter
    ) : VaccinationCenterRepository {
        return VaccinationCenterRepository(vaccinationCenterRemoteDataSource, dataBaseVaccinationCenter)
    }
}