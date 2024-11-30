package org.example.project

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyListResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)