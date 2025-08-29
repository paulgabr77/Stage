package com.example.stage.data.repository

import com.example.stage.data.remote.api.ExchangeRateApi
import com.example.stage.data.remote.dto.ExchangeRateResponse
import com.example.stage.data.remote.dto.ConversionResponse
import com.example.stage.data.remote.dto.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository pentru operațiile cu rate-urile de schimb.
 * Combină API-ul extern cu cache-ul local și logica de business.
 */
class ExchangeRateRepository(
    private val exchangeRateApi: ExchangeRateApi
) {
    
    /**
     * Obține rate-urile de schimb pentru o monedă de bază.
     * 
     * @param baseCurrency moneda de bază (implicit RON)
     * @return Flow cu rate-urile de schimb
     */
    fun getLatestRates(baseCurrency: String = "RON"): Flow<ExchangeRateResponse> = flow {
        try {
            val response = exchangeRateApi.getLatestRates(baseCurrency)
            emit(response)
        } catch (e: Exception) {
            // În caz de eroare, emitem un răspuns cu rate-uri implicite
            emit(ExchangeRateResponse(
                success = false,
                timestamp = System.currentTimeMillis(),
                baseCurrency = baseCurrency,
                date = "",
                rates = getDefaultRates(baseCurrency)
            ))
        }
    }
    
    /**
     * Convertește o sumă între două monede.
     * 
     * @param fromCurrency moneda de la care se convertește
     * @param toCurrency moneda către care se convertește
     * @param amount suma de convertit
     * @return Flow cu rezultatul conversiei
     */
    fun convertCurrency(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Flow<ConversionResponse> = flow {
        try {
            val response = exchangeRateApi.convertCurrency(fromCurrency, toCurrency, amount)
            emit(response)
        } catch (e: Exception) {
            // În caz de eroare, emitem un răspuns cu conversie aproximativă
            emit(ConversionResponse(
                success = false,
                query = com.example.stage.data.remote.dto.ConversionQuery(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    amount = amount
                ),
                info = com.example.stage.data.remote.dto.ConversionInfo(
                    timestamp = System.currentTimeMillis(),
                    rate = getDefaultRate(fromCurrency, toCurrency)
                ),
                result = amount * getDefaultRate(fromCurrency, toCurrency)
            ))
        }
    }
    
    /**
     * Obține rate-urile pentru monedele specificate.
     * 
     * @param baseCurrency moneda de bază
     * @param targetCurrencies lista de monede țintă
     * @return Flow cu rate-urile de schimb
     */
    fun getSpecificRates(
        baseCurrency: String,
        targetCurrencies: List<String>
    ): Flow<ExchangeRateResponse> = flow {
        try {
            val symbols = targetCurrencies.joinToString(",")
            val response = exchangeRateApi.getSpecificRates(baseCurrency, symbols)
            emit(response)
        } catch (e: Exception) {
            // În caz de eroare, emitem rate-uri implicite
            val defaultRates = targetCurrencies.associateWith { 
                getDefaultRate(baseCurrency, it) 
            }
            emit(ExchangeRateResponse(
                success = false,
                timestamp = System.currentTimeMillis(),
                baseCurrency = baseCurrency,
                date = "",
                rates = defaultRates
            ))
        }
    }
    
    /**
     * Convertește o sumă folosind rate-urile din cache.
     * 
     * @param amount suma de convertit
     * @param fromCurrency moneda de la care se convertește
     * @param toCurrency moneda către care se convertește
     * @param rates rate-urile de schimb disponibile
     * @return suma convertită
     */
    fun convertWithRates(
        amount: Double,
        fromCurrency: String,
        toCurrency: String,
        rates: Map<String, Double>
    ): Double {
        return if (fromCurrency == toCurrency) {
            amount
        } else {
            val rate = rates[toCurrency] ?: getDefaultRate(fromCurrency, toCurrency)
            amount * rate
        }
    }
    
    /**
     * Obține rate-urile implicite pentru cazul când API-ul nu este disponibil.
     * 
     * @param baseCurrency moneda de bază
     * @return mapă cu rate-urile implicite
     */
    private fun getDefaultRates(baseCurrency: String): Map<String, Double> {
        return when (baseCurrency) {
            "RON" -> mapOf(
                "EUR" to 0.20,
                "USD" to 0.22,
                "GBP" to 0.16,
                "CHF" to 0.19,
                "JPY" to 24.0,
                "CAD" to 0.28,
                "AUD" to 0.30
            )
            "EUR" -> mapOf(
                "RON" to 5.0,
                "USD" to 1.1,
                "GBP" to 0.8,
                "CHF" to 0.95,
                "JPY" to 120.0,
                "CAD" to 1.4,
                "AUD" to 1.5
            )
            else -> mapOf(
                "RON" to 4.5,
                "EUR" to 0.9,
                "USD" to 1.0,
                "GBP" to 0.73,
                "CHF" to 0.86,
                "JPY" to 110.0,
                "CAD" to 1.27,
                "AUD" to 1.36
            )
        }
    }
    
    /**
     * Obține un rate implicit între două monede.
     * 
     * @param fromCurrency moneda de la care se convertește
     * @param toCurrency moneda către care se convertește
     * @return rate-ul implicit
     */
    private fun getDefaultRate(fromCurrency: String, toCurrency: String): Double {
        return when {
            fromCurrency == toCurrency -> 1.0
            fromCurrency == "RON" && toCurrency == "EUR" -> 0.20
            fromCurrency == "RON" && toCurrency == "USD" -> 0.22
            fromCurrency == "EUR" && toCurrency == "RON" -> 5.0
            fromCurrency == "USD" && toCurrency == "RON" -> 4.5
            else -> 1.0 // Rate implicit pentru alte combinații
        }
    }
    
    /**
     * Obține toate monedele suportate.
     * 
     * @return lista cu toate monedele
     */
    fun getSupportedCurrencies(): List<Currency> {
        return Currency.values().toList()
    }
}
