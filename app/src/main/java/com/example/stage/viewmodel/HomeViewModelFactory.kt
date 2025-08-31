package com.example.stage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stage.data.repository.PostRepository
import com.example.stage.data.repository.ExchangeRateRepository
import com.example.stage.utils.SharedPreferencesManager

/**
 * Factory pentru crearea HomeViewModel cu dependențele necesare.
 */
class HomeViewModelFactory(
    private val postRepository: PostRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(postRepository, exchangeRateRepository, sharedPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
