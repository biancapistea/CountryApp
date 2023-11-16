package com.example.data.repository

import com.example.data.dto.WordStatusDto
import com.example.data.network.ApiException
import com.example.data.preferences.GamificationPersistenceStore
import com.example.data.util.GamificationKey.QUESTION_STATUS_KEY
import com.example.data.util.GamificationKey.WORD_STATUS_KEY
import com.example.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

interface GameRepository {
    fun saveQuestionStatus(status: String)
    fun getQuestionStatus(): Flow<Resource<String>?>
    fun saveCurrentStatusOfWord(statusQuestion: WordStatusDto)
    fun getCurrentStatusOfWord(): Flow<Resource<WordStatusDto>?>
}

internal class GameRepositoryImpl @Inject constructor(private val gamificationPersistenceStore: GamificationPersistenceStore) :
    GameRepository {
    override fun saveQuestionStatus(status: String) {
        gamificationPersistenceStore.saveQuestionStatus(QUESTION_STATUS_KEY, status)
    }

    override fun getQuestionStatus(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val questionStatus = gamificationPersistenceStore.getQuestionStatus(QUESTION_STATUS_KEY)
            emit(
                Resource.Success(questionStatus ?: "Not found")
            )
        } catch (e: ApiException) {
            emit(Resource.Error("Not found"))
        }
    }

    override fun saveCurrentStatusOfWord(statusQuestion: WordStatusDto) {
        gamificationPersistenceStore.saveCurrentStatusOfWord(WORD_STATUS_KEY, statusQuestion)
    }

    override fun getCurrentStatusOfWord(): Flow<Resource<WordStatusDto>> = flow {
        try {
            emit(Resource.Loading())
            val currentStatusWord =
                gamificationPersistenceStore.getCurrentStatusOfWord(WORD_STATUS_KEY)
            emit(
                Resource.Success(currentStatusWord ?: throw ApiException(message = "Not found"))
            )
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: ApiException) {
            emit(Resource.Error("Not found"))
        }
    }
}