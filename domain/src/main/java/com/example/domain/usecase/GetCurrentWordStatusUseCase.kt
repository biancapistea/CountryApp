package com.example.domain.usecase

import android.util.Log
import com.example.data.repository.GameRepository
import com.example.domain.model.WordStatus
import com.example.domain.model.WordStatusDtoToModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetCurrentWordStatusUseCase {
    fun getCurrentWordStatusUseCase(): Flow<WordStatus>?
}

internal class GetCurrentWordStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) :
    GetCurrentWordStatusUseCase {
    override fun getCurrentWordStatusUseCase(): Flow<WordStatus> = flow {
        gameRepository.getCurrentStatusOfWord().map { wordStatus ->
            wordStatus?.data?.let { WordStatusDtoToModelMapper.mapToModel(it) }
        }
            .catch {
                Log.d("EROARE", "${it.message}")
            }
    }
}