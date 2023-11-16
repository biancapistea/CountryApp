package com.example.data.preferences

import com.example.data.dto.WordStatusDto

interface GamificationPersistenceStore {
    fun saveQuestionStatus(key: String, status: String)
    fun getQuestionStatus(key: String): String?
    fun saveCurrentStatusOfWord(key: String, statusQuestion: WordStatusDto)
    fun getCurrentStatusOfWord(key: String): WordStatusDto?

}