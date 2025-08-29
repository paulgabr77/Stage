package com.example.stage.data.remote.api

import com.example.stage.data.remote.dto.ExchangeRateResponse
import com.example.stage.data.remote.dto.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfața pentru API-ul de conversie valutară.
 * Definește toate endpoint-urile pentru obținerea rate-urilor de schimb.
 */
interface ExchangeRateApi {
    
    /**
     * Obține toate rate-urile de schimb pentru o monedă de bază.
     * 
     * @param base moneda de bază (ex: "RON", "EUR", "USD")
     * @return răspunsul cu toate rate-urile de schimb
     */
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String = "RON"
    ): ExchangeRateResponse
    
    /**
     * Convertește o sumă între două monede.
     * 
     * @param from moneda de la care se convertește
     * @param to moneda către care se convertește
     * @param amount suma de convertit
     * @return răspunsul cu rezultatul conversiei
     */
    @GET("convert")
    suspend fun convertCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): ConversionResponse
    
    /**
     * Obține rate-urile de schimb pentru monedele specificate.
     * 
     * @param base moneda de bază
     * @param symbols lista de monede separate prin virgulă (ex: "EUR,USD,GBP")
     * @return răspunsul cu rate-urile de schimb
     */
    @GET("latest")
    suspend fun getSpecificRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): ExchangeRateResponse
}
