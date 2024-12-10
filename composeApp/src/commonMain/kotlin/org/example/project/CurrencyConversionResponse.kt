package org.example.project

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyConversionResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val query: Query,
    val info: Info,
    val result: Double
)

@Serializable
data class Query(
    val from: String,
    val to: String,
    val amount: Double
)

@Serializable
data class Info(
    val timestamp: Long,
    val quote: Double
)