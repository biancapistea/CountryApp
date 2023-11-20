package com.example.domain.usecase

import com.example.data.network.ApiException
import com.example.data.repository.GameRepository
import com.example.domain.model.WordStatus
import com.example.domain.model.WordStatusDtoToModelMapper
import javax.inject.Inject

interface GetCurrentWordStatusUseCase {
    fun getCurrentWordStatusUseCase(countryName: String): WordStatus
}

internal class GetCurrentWordStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) :
    GetCurrentWordStatusUseCase {
    override fun getCurrentWordStatusUseCase(countryName: String): WordStatus {
        return try {
            val currentWordStatusDto = gameRepository.getCurrentStatusOfWord(countryName)
            WordStatusDtoToModelMapper.mapToModel(currentWordStatusDto)
        } catch (exception: ApiException) {
            WordStatus(emptySet(), emptySet(), emptySet())
        }
    }
}