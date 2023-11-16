package com.example.data.preferences

import android.content.SharedPreferences
import com.example.data.dto.WordStatusDto
import com.example.data.util.GamificationKey.QUESTION_STATUS_KEY
import com.example.data.util.GamificationKey.WORD_STATUS_KEY
import com.google.gson.Gson
import javax.inject.Inject

internal class GamificationPersistenceStoreImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    GamificationPersistenceStore {
    override fun saveQuestionStatus(key: String, status: String) {
        sharedPreferences.edit().putString(QUESTION_STATUS_KEY, status).apply()
    }

    override fun getQuestionStatus(key: String): String? {
        return sharedPreferences.getString(QUESTION_STATUS_KEY, null)
    }

    override fun saveCurrentStatusOfWord(key: String, statusQuestion: WordStatusDto) {
        val statusToString = Gson().toJson(statusQuestion)
        sharedPreferences.edit().putString(WORD_STATUS_KEY, statusToString).apply()
    }

    override fun getCurrentStatusOfWord(key: String): WordStatusDto? {
        val wordStatus = sharedPreferences.getString(WORD_STATUS_KEY, null)
        return Gson().fromJson(wordStatus, WordStatusDto::class.java) ?: null
    }
}