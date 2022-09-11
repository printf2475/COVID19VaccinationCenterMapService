package com.example.covid19vaccinationcentermapservice.di

import com.example.covid19vaccinationcentermapservice.BuildConfig
import com.example.covid19vaccinationcentermapservice.data.remote.VaccinationCenterRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideVaccinationCenterBaseUrl(): String = BuildConfig.VACCINATION_CENTER_BASE_URL


    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()


    @Singleton
    @Provides
    fun provideCoinRetrofit(okHttpClient: OkHttpClient): VaccinationCenterRestApi=
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(provideVaccinationCenterBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VaccinationCenterRestApi::class.java)


}