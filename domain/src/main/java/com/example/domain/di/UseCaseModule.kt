package com.example.domain.di

import com.example.domain.usecase.LoadAllCountriesUseCase
import com.example.domain.usecase.LoadAllCountriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    internal abstract fun bindsLoadAllCountriesUseCase(
        loadAllCountriesUseCaseImpl: LoadAllCountriesUseCaseImpl
    ): LoadAllCountriesUseCase
}