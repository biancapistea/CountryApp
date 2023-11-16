package com.example.domain.di

import com.example.domain.usecase.GetCurrentWordStatusUseCase
import com.example.domain.usecase.GetCurrentWordStatusUseCaseImpl
import com.example.domain.usecase.GetQuestionStatusUseCase
import com.example.domain.usecase.GetQuestionStatusUseCaseImpl
import com.example.domain.usecase.LoadAllCountriesUseCase
import com.example.domain.usecase.LoadAllCountriesUseCaseImpl
import com.example.domain.usecase.SaveCurrentWordStatusUseCase
import com.example.domain.usecase.SaveCurrentWordStatusUseCaseImpl
import com.example.domain.usecase.SaveQuestionStatusUseCase
import com.example.domain.usecase.SaveQuestionStatusUseCaseImpl
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

    @Binds
    internal abstract fun bindsGetQuestionStatusUseCase(
        getQuestionStatusUseCaseImpl: GetQuestionStatusUseCaseImpl
    ): GetQuestionStatusUseCase

    @Binds
    internal abstract fun bindsSaveQuestionStatusUseCase(
        saveQuestionStatusUseCaseImpl: SaveQuestionStatusUseCaseImpl
    ): SaveQuestionStatusUseCase

    @Binds
    internal abstract fun bindsSaveCurrentWordStatusUseCase(
        saveCurrentWordStatusUseCaseImpl: SaveCurrentWordStatusUseCaseImpl
    ): SaveCurrentWordStatusUseCase


    @Binds
    internal abstract fun bindsGetCurrentWordStatusUseCase(
        getCurrentWordStatusUseCaseImpl: GetCurrentWordStatusUseCaseImpl
    ): GetCurrentWordStatusUseCase
}