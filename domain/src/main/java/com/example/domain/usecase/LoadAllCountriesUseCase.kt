package com.example.domain.usecase

import android.util.Log
import com.example.data.network.ApiException
import com.example.data.repository.CountryRepository
import com.example.data.service.DispatchersProvider
import com.example.domain.model.Country
import com.example.domain.model.CountryNameDtoToModelMapper
import com.example.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface LoadAllCountriesUseCase {
    fun loadAllCountries(): Flow<Resource<List<Country>>>
}

internal class LoadAllCountriesUseCaseImpl @Inject constructor(
    private val repository: CountryRepository, private val dispatchersProvider: DispatchersProvider
) : LoadAllCountriesUseCase {

    override fun loadAllCountries(): Flow<Resource<List<Country>>> = flow {
        Log.d("A11:", "before calling the repo")
        try {
            emit(Resource.Loading())
            Log.d("A11:", "after emitting loading")
            val countries = repository.loadAllCountries().map { countries ->
                CountryNameDtoToModelMapper.map(countries)
            }
            emit(
                Resource.Success(countries)
            )
        } catch (exception: ApiException) {
            Log.d("A11:", "before emitting the error")
            emit(Resource.Error(exception.message ?: "An error occurred"))
        }
    }.flowOn(dispatchersProvider.io())
}