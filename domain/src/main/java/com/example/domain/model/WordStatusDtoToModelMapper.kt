package com.example.domain.model

import com.example.data.dto.WordStatusDto

object WordStatusDtoToModelMapper {
    fun map(wordStatus: WordStatus): WordStatusDto {
        return WordStatusDto(
            correctLetters = wordStatus.correctLetters,
            usedLetters = wordStatus.usedLetters,
            wrongLetters = wordStatus.wrongLetters
        )
    }

    fun mapToModel(wordStatus: WordStatusDto): WordStatus {
        return WordStatus(
            correctLetters = wordStatus.correctLetters,
            usedLetters = wordStatus.usedLetters,
            wrongLetters = wordStatus.wrongLetters
        )
    }
}