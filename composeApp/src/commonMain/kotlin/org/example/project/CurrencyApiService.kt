package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.encodedPath
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

object CurrencyApiService {
    private const val APIURL = "https://api.currencylayer.com/"
    private const val TOKEN = ""

    suspend fun convertCurrency(fromCurrency: String, toCurrency: String, amount: String): Double {
        val jsonString = requestConversion(fromCurrency, toCurrency, amount)
        if (jsonString != null) {
            try {
                val response = Json.decodeFromString<CurrencyConversionResponse>(jsonString)
                return response.result
            } catch (e: Exception) {
                println(e)
            }
        }
        throw Exception("Conversion failed")
    }

    suspend fun getCurrencies(): List<String> {
        val jsonString = requestCurrencyList()
        if (jsonString != null) {
            try {
                val response = Json.decodeFromString<CurrencyListResponse>(jsonString)
                return response.currencies.keys.toList()
            } catch (e: Exception) {
                println(e)
            }
        }
        throw Exception("Fetching currencies failed")
    }

    private suspend fun requestConversion(fromCurrency: String, toCurrency: String, amount: String): String? {
        val client = HttpClient()
        return try {
            val response: HttpResponse = client.get(APIURL) {
                url {
                    encodedPath = "convert"
                    parameters.append("access_key", TOKEN)
                    parameters.append("from", fromCurrency)
                    parameters.append("to", toCurrency)
                    parameters.append("amount", amount.toString())
                    parameters.append("format", "1")
                }
            }
            response.bodyAsText()
        } catch (e: Exception) {
            println(e)
            null
        } finally {
            client.close()
        }
    }

    private suspend fun requestCurrencyList(): String? {
        val client = HttpClient()
        return try {
            val response: HttpResponse = client.get(APIURL) {
                url {
                    encodedPath = "list"
                    parameters.append("access_key", TOKEN)
                    parameters.append("format", "1")
                }
            }
            response.bodyAsText()
        } catch (e: Exception) {
            println(e)
            null
        } finally {
            client.close()
        }
    }
}