package com.example.domain.usecase

import android.util.Log
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface GetQuestionStatusUseCase {
  fun getQuestionStatus(): Flow<String>?
}

internal class GetQuestionStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) : GetQuestionStatusUseCase {
    override fun getQuestionStatus(): Flow<String>  = flow {
        gameRepository.getQuestionStatus().catch {
            Log.d("EROARE", "${it.message}")
        }
    }
}