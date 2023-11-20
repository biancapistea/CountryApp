package com.example.data.repository

import com.example.data.dto.WordStatusDto
import com.example.data.network.ApiException
import com.example.data.preferences.GamificationPersistenceStore
import com.example.data.util.GamificationKey.QUESTION_STATUS_KEY
import com.example.data.util.GamificationKey.WORD_STATUS_KEY
import java.io.IOException
import javax.inject.Inject

interface GameRepository {
    fun saveQuestionStatus(status: String, countryName: String)
    fun getQuestionStatus(countryName: String): String
    fun saveCurrentStatusOfWord(statusQuestion: WordStatusDto, countryName: String)
    fun getCurrentStatusOfWord(countryName: String): WordStatusDto
}

internal class GameRepositoryImpl @Inject constructor(private val gamificationPersistenceStore: GamificationPersistenceStore) :
    GameRepository {
    override fun saveQuestionStatus(status: String, countryName: String) {
        gamificationPersistenceStore.saveQuestionStatus("${QUESTION_STATUS_KEY}_${countryName.replace("\\s".toRegex(), "")}", status)
    }

    override fun getQuestionStatus(countryName: String): String {
       return try {
           gamificationPersistenceStore.getQuestionStatus("${QUESTION_STATUS_KEY}_${countryName.replace("\\s".toRegex(), "")}")
        } catch (e: ApiException) {
           throw ApiException(message = "Not found")
        }
    }

    override fun saveCurrentStatusOfWord(statusQuestion: WordStatusDto, countryName: String) {
        gamificationPersistenceStore.saveCurrentStatusOfWord("${WORD_STATUS_KEY}_${countryName.replace("\\s".toRegex(), "")}", statusQuestion)
    }

    override fun getCurrentStatusOfWord(countryName: String): WordStatusDto {
       return try {
           gamificationPersistenceStore.getCurrentStatusOfWord("${WORD_STATUS_KEY}_${countryName.replace("\\s".toRegex(), "")}")
        } catch (e: IOException) {
            throw ApiException (message =  "Couldn't reach server. Check your internet connection.")
        }
    }
}