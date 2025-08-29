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
    
    /**
     * Timeout pentru conexiuni HTTP (30 secunde).
     */
    private const val TIMEOUT_SECONDS = 30L
    
    /**
     * Client HTTP configurat cu logging și timeout.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(createLoggingInterceptor())
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    /**
     * Creează interceptorul de logging pentru debugging.
     * Afișează toate request-urile și response-urile în loguri.
     */
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    /**
     * Instanța Retrofit pentru API-ul de conversie valutară.
     */
    private val exchangeRateRetrofit = Retrofit.Builder()
        .baseUrl(EXCHANGE_RATE_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * API pentru conversia valutară.
     */
    val exchangeRateApi: ExchangeRateApi = exchangeRateRetrofit.create(ExchangeRateApi::class.java)
    
    /**
     * Creează o nouă instanță de ExchangeRateApi cu URL personalizat.
     * Util pentru testare sau pentru a schimba API-ul.
     * 
     * @param baseUrl URL-ul de bază pentru API
     * @return instanța API-ului
     */
    fun createExchangeRateApi(baseUrl: String): ExchangeRateApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }
}
