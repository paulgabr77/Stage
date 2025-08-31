package com.example.stage.utils

import android.content.Context
import com.example.stage.data.local.database.AppDatabase
import com.example.stage.data.repository.UserRepository

/**
 * Provider pentru dependențele aplicației.
 * Furnizează instanțe pentru Repository-uri și alte dependențe.
 */
object DependencyProvider {
    
    private var userRepository: UserRepository? = null
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
     * Șterge toate dependențele (pentru testare).
     */
    fun clear() {
        userRepository = null
    }
}
