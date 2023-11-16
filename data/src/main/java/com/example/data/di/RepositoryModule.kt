package com.example.data.di

import com.example.data.preferences.GamificationPersistenceStore
import com.example.data.preferences.GamificationPersistenceStoreImpl
import com.example.data.repository.CountryRepository
import com.example.data.repository.CountryRepositoryImpl
import com.example.data.repository.GameRepository
import com.example.data.repository.GameRepositoryImpl
import com.example.data.service.DispatchersProvider
import com.example.data.service.RuntimeDispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    internal abstract fun bindsCountryRepository(
        countryRepositoryImpl: CountryRepositoryImpl
    ): CountryRepository

    @Binds
    internal abstract fun bindsGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository

    @Binds
    internal abstract fun bindsDispatcherProvider(
        runtimeDispatchersProvider: RuntimeDispatchersProvider
    ): DispatchersProvider

    @Binds
    internal abstract fun bindsPersistenceStore(
        gamificationPersistenceStoreImpl: GamificationPersistenceStoreImpl
    ): GamificationPersistenceStore

}