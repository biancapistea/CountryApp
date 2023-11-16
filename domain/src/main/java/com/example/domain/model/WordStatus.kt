package com.example.domain.model

data class WordStatus (
    val correctLetters: Set<Char>,
    val usedLetters: Set<Char>,
    val wrongLetters: Set<Char>
)