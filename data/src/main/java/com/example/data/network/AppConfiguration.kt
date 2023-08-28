package com.example.data.network

class AppConfiguration {
    private val baseUrl = "https://restcountries.com"

    fun getBaseUrl(): String {
        return baseUrl
    }
}