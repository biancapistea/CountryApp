package com.example.data.preferences

import android.content.SharedPreferences
import com.example.data.dto.WordStatusDto
import com.google.gson.Gson
import javax.inject.Inject

internal class GamificationPersistenceStoreImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    GamificationPersistenceStore {
    override fun saveQuestionStatus(key: String, status: String) {
        sharedPreferences.edit().putString(key, status).apply()
    }

    override fun getQuestionStatus(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    override fun saveCurrentStatusOfWord(key: String, statusQuestion: WordStatusDto) {
        val statusToString = Gson().toJson(statusQuestion)
        sharedPreferences.edit().putString(key, statusToString).apply()
    }

    override fun getCurrentStatusOfWord(key: String): WordStatusDto {
        val wordStatus = sharedPreferences.getString(key, "")
        return Gson().fromJson(wordStatus, WordStatusDto::class.java) ?: WordStatusDto(emptySet(), emptySet(), emptySet())
    }
}