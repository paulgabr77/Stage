package com.example.stage.data.remote.network

import com.example.stage.data.remote.api.ExchangeRateApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Modulul de rețea pentru configurarea Retrofit și API-urilor.
 * Gestionează toate conexiunile HTTP către servere externe.
 */
object NetworkModule {
    
    /**
     * URL-ul de bază pentru API-ul de conversie valutară.
     * Folosim exchangerate-api.com (gratuit, fără API key necesar)
     */
    private const val EXCHANGE_RATE_BASE_URL = "https://api.exchangerate-api.com/v4/"
    private const val TIMEOUT_SECONDS = 30L

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(createLoggingInterceptor())
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val exchangeRateRetrofit = Retrofit.Builder()
        .baseUrl(EXCHANGE_RATE_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val exchangeRateApi: ExchangeRateApi = exchangeRateRetrofit.create(ExchangeRateApi::class.java)

    fun createExchangeRateApi(baseUrl: String): ExchangeRateApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }
}
