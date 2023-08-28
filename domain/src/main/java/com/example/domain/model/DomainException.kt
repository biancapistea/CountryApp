package com.example.domain.model

data class DomainException(val noInternet: Boolean, override val message: String?) : Exception()