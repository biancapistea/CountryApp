package com.example.domain.usecase

import com.example.data.network.ApiException
import com.example.data.repository.GameRepository
import javax.inject.Inject

interface GetQuestionStatusUseCase {
    fun getQuestionStatus(countryName: String): String
}

internal class GetQuestionStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) :
    GetQuestionStatusUseCase {
    override fun getQuestionStatus(countryName: String): String {
        return try {
            gameRepository.getQuestionStatus(countryName)
        } catch (exception: ApiException) {
            ""
        }
    }
}