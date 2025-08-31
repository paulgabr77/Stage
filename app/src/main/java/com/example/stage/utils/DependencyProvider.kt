package com.example.stage.utils

import android.content.Context
import com.example.stage.data.local.database.AppDatabase
import com.example.stage.data.repository.UserRepository
import com.example.stage.data.repository.PostRepository
import com.example.stage.data.repository.ExchangeRateRepository

/**
 * Provider pentru dependențele aplicației.
 * Furnizează instanțe pentru Repository-uri și alte dependențe.
 */
object DependencyProvider {
    
    private var userRepository: UserRepository? = null
    private var postRepository: PostRepository? = null
    private var exchangeRateRepository: ExchangeRateRepository? = null
    private var database: AppDatabase? = null
    
    /**
     * Inițializează dependențele.
     * Trebuie apelat în MainActivity sau Application.
     * 
     * @param context contextul aplicației
     */
    fun initialize(context: Context) {
        try {
            database = AppDatabase.getDatabase(context)
            userRepository = UserRepository(database!!.userDao())
            postRepository = PostRepository(database!!.postDao(), database!!.carDetailsDao())
            exchangeRateRepository = ExchangeRateRepository(com.example.stage.data.remote.network.NetworkModule.exchangeRateApi)
            println("DEBUG: DependencyProvider initialized successfully")
        } catch (e: Exception) {
            println("DEBUG: DependencyProvider initialization failed: ${e.message}")
            throw e
        }
    }
    
    /**
     * Obține instanța UserRepository.
     * 
     * @return instanța UserRepository
     * @throws IllegalStateException dacă nu a fost inițializat
     */
    fun getUserRepository(): UserRepository {
        return userRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }
    
    /**
     * Obține instanța PostRepository.
     * 
     * @return instanța PostRepository
     * @throws IllegalStateException dacă nu a fost inițializat
     */
    fun getPostRepository(): PostRepository {
        return postRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }
    
    /**
     * Obține instanța ExchangeRateRepository.
     * 
     * @return instanța ExchangeRateRepository
     * @throws IllegalStateException dacă nu a fost inițializat
     */
    fun getExchangeRateRepository(): ExchangeRateRepository {
        return exchangeRateRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }
    
    /**
     * Șterge toate dependențele (pentru testare).
     */
    fun clear() {
        userRepository = null
        postRepository = null
        exchangeRateRepository = null
    }
}
