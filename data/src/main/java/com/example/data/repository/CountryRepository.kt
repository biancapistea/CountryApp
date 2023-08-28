package com.example.data.repository

import android.os.Looper
import android.util.Log
import com.example.data.dto.CountryDto
import com.example.data.network.CountryApi
import com.example.data.service.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface CountryRepository {
    fun loadAllCountries(): Flow<List<CountryDto>>
}

internal class CountryRepositoryImpl @Inject constructor(
    private val countryApi: CountryApi,
    private val dispatchersProvider: DispatchersProvider
) : CountryRepository {

    override fun loadAllCountries(): Flow<List<CountryDto>> = flow {
        Log.d("loadAllCountriedByName()", "loadAllCountriedByName() was called")
        Log.d("I'm running on (loadAllCountriesByName from repo)", "${Thread.currentThread()}")
        Log.d("RULEZ PE MAIN THREAD??? din viewmodel", "${Looper.getMainLooper().thread == Thread.currentThread()}")
        val data = countryApi.loadAllCountries()
        Log.d("country repository data", data.body().toString())
        emit(data.body() ?: listOf())
    }.flowOn(dispatchersProvider.io())

}