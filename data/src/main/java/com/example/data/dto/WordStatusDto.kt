package com.example.data.dto

data class WordStatusDto(
    val correctLetters: Set<Char>,
    val usedLetters: Set<Char>,
    val wrongLetters: Set<Char>
)