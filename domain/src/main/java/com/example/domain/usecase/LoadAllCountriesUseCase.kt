package com.example.domain.usecase

import android.util.Log
import com.example.data.repository.CountryRepository
import com.example.domain.model.Country
import com.example.domain.model.CountryNameDtoToModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LoadAllCountriesUseCase {
    fun loadAllCountries(): Flow<List<Country>>
}

internal class LoadAllCountriesUseCaseImpl @Inject constructor(private val repository: CountryRepository) :
    LoadAllCountriesUseCase {

    override fun loadAllCountries(): Flow<List<Country>> =
        repository.loadAllCountries().map { countryNames ->
            Log.d("countries returned", countryNames.toString())
            Log.d("I'm running on (loadAllCountriesByName from usecase)", "${Thread.currentThread()}")
            countryNames.map { CountryNameDtoToModelMapper.map(it) }
        }.catch {
            //emit(Result.Error(it))
            Log.d("country name error", it.toString())
        }

    //TODO: handle error also
//    =
//    loginRepository.login(email, password, keepMeLoggedInIsChecked).map {
//        Result.Success(loginMapper.map(it)) as Result<LoginModel>
//    }.catch {
//        emit(Result.Error(it))
//    }
}