package com.example.stage.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Răspunsul de la API-ul de conversie valutară.
 * Conține rate-urile de schimb pentru diferite monede.
 */
data class ExchangeRateResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("base")
    val baseCurrency: String,
    
    @SerializedName("date")
    val date: String,
    
    @SerializedName("rates")
    val rates: Map<String, Double>
)

/**
 * Răspunsul pentru o conversie specifică între două monede.
 */
data class ConversionResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("query")
    val query: ConversionQuery,
    
    @SerializedName("info")
    val info: ConversionInfo,
    
    @SerializedName("result")
    val result: Double
)

/**
 * Detaliile query-ului de conversie.
 */
data class ConversionQuery(
    @SerializedName("from")
    val fromCurrency: String,
    
    @SerializedName("to")
    val toCurrency: String,
    
    @SerializedName("amount")
    val amount: Double
)

/**
 * Informații despre conversie.
 */
data class ConversionInfo(
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("rate")
    val rate: Double
)

/**
 * Enum pentru monedele suportate.
 */
enum class Currency(val code: String, val symbol: String, val displayName: String) {
    RON("RON", "lei", "Leu românesc"),
    EUR("EUR", "€", "Euro"),
    USD("USD", "$", "Dolar american"),
    GBP("GBP", "£", "Liră sterlină"),
    CHF("CHF", "CHF", "Franc elvețian"),
    JPY("JPY", "¥", "Yen japonez"),
    CAD("CAD", "C$", "Dolar canadian"),
    AUD("AUD", "A$", "Dolar australian")
}
