package com.example.stage.data.remote.api

import com.example.stage.data.remote.dto.ExchangeRateResponse
import com.example.stage.data.remote.dto.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String = "RON"
    ): ExchangeRateResponse

    @GET("convert")
    suspend fun convertCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): ConversionResponse

    @GET("latest")
    suspend fun getSpecificRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): ExchangeRateResponse
}
