package com.example.data.network

import com.example.data.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Headers

internal interface CountryApi {
    @GET("/v3.1/all")
    @Headers("Cache-Control: no-cache")
    suspend fun loadAllCountries(): List<CountryDto>
}