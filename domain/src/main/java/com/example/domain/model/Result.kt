package com.example.domain.model

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: DomainException) : Result<Nothing>()
    object EmptySuccess : Result<Nothing>()
}