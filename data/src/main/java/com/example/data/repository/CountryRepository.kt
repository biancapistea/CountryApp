package com.example.data.repository

import android.util.Log
import com.example.data.dto.CountryDto
import com.example.data.network.ApiException
import com.example.data.network.CountryApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface CountryRepository {
    suspend fun loadAllCountries(): List<CountryDto>
}

internal class CountryRepositoryImpl @Inject constructor(
    private val countryApi: CountryApi
) : CountryRepository {
    override suspend fun loadAllCountries(): List<CountryDto> {
        Log.d("A11:", "before api call")
        return try {
            countryApi.loadAllCountries()
        } catch (e: HttpException) {
            throw ApiException(message = "Could not retreive data. An unexpected error occured")
        } catch (e: IOException) {
            throw ApiException(message = "Couldn't reach server. Check your internet connection.")
        }
    }
}