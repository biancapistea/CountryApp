package com.example.domain.usecase

import com.example.data.repository.GameRepository
import javax.inject.Inject

interface SaveQuestionStatusUseCase {
    fun saveQuestionStatus(status: String, countryName: String)
}

internal class SaveQuestionStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) :
    SaveQuestionStatusUseCase {
    override fun saveQuestionStatus(status: String, countryName: String) =
        gameRepository.saveQuestionStatus(status, countryName)
}