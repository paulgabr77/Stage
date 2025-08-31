package com.example.stage.utils

import android.content.Context
import com.example.stage.data.local.database.AppDatabase
import com.example.stage.data.repository.UserRepository
import com.example.stage.data.repository.PostRepository
import com.example.stage.data.repository.ExchangeRateRepository

object DependencyProvider {
    
    private var userRepository: UserRepository? = null
    private var postRepository: PostRepository? = null
    private var exchangeRateRepository: ExchangeRateRepository? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null
    private var database: AppDatabase? = null

    fun initialize(context: Context) {
        try {
            database = AppDatabase.getDatabase(context)
            userRepository = UserRepository(database!!.userDao())
            postRepository = PostRepository(database!!.postDao(), database!!.carDetailsDao())
            exchangeRateRepository = ExchangeRateRepository(com.example.stage.data.remote.network.NetworkModule.exchangeRateApi)
            sharedPreferencesManager = SharedPreferencesManager(context)
            println("DEBUG: DependencyProvider initialized successfully")
        } catch (e: Exception) {
            println("DEBUG: DependencyProvider initialization failed: ${e.message}")
            throw e
        }
    }

    fun getUserRepository(): UserRepository {
        return userRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }

    fun getPostRepository(): PostRepository {
        return postRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }

    fun getExchangeRateRepository(): ExchangeRateRepository {
        return exchangeRateRepository ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }

    fun getSharedPreferencesManager(): SharedPreferencesManager {
        return sharedPreferencesManager ?: throw IllegalStateException(
            "DependencyProvider nu a fost inițializat. Apelă initialize() înainte."
        )
    }

    fun clear() {
        userRepository = null
        postRepository = null
        exchangeRateRepository = null
        sharedPreferencesManager = null
    }
}
