package com.example.domain.usecase

import com.example.data.repository.GameRepository
import com.example.domain.model.WordStatus
import com.example.domain.model.WordStatusDtoToModelMapper
import javax.inject.Inject

interface SaveCurrentWordStatusUseCase {
    fun saveCurrentStatusUseCase(wordStatus: WordStatus)
}

internal class SaveCurrentWordStatusUseCaseImpl @Inject constructor(private val gameRepository: GameRepository) :
    SaveCurrentWordStatusUseCase {
    override fun saveCurrentStatusUseCase(wordStatus: WordStatus) {
        gameRepository.saveCurrentStatusOfWord(WordStatusDtoToModelMapper.map(wordStatus))
    }
}