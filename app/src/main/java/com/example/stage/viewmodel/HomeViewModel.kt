package com.example.stage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.PostCategory
import com.example.stage.data.remote.dto.Currency
import com.example.stage.data.repository.PostRepository
import com.example.stage.data.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pentru ecranul principal (lista de anunțuri).
 * Gestionează afișarea, filtrarea și conversia valutară pentru anunțuri.
 */
class HomeViewModel(
    private val postRepository: PostRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {
    
    // State pentru lista de anunțuri
    private val _postsState = MutableStateFlow<PostsState>(PostsState.Loading)
    val postsState: StateFlow<PostsState> = _postsState.asStateFlow()
    
    // State pentru conversia valutară
    private val _currencyState = MutableStateFlow<CurrencyState>(CurrencyState.Idle)
    val currencyState: StateFlow<CurrencyState> = _currencyState.asStateFlow()
    
    // Filtre
    private val _selectedCategory = MutableStateFlow<PostCategory?>(null)
    val selectedCategory: StateFlow<PostCategory?> = _selectedCategory.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _priceRange = MutableStateFlow<Pair<Double?, Double?>>(null to null)
    val priceRange: StateFlow<Pair<Double?, Double?>> = _priceRange.asStateFlow()
    
    // Moneda selectată
    private val _selectedCurrency = MutableStateFlow(Currency.RON)
    val selectedCurrency: StateFlow<Currency> = _selectedCurrency.asStateFlow()
    
    // Rate-urile de schimb
    private val _exchangeRates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val exchangeRates: StateFlow<Map<String, Double>> = _exchangeRates.asStateFlow()
    
    init {
        loadPosts()
        loadExchangeRates()
    }
    
    /**
     * Încarcă toate anunțurile active.
     */
    private fun loadPosts() {
        viewModelScope.launch {
            try {
                _postsState.value = PostsState.Loading
                
                // Combină toate filtrele
                combine(
                    postRepository.getAllActivePosts(),
                    selectedCategory,
                    searchQuery,
                    priceRange
                ) { posts, category, query, range ->
                    var filteredPosts = posts
                    
                    // Filtrare după categorie
                    if (category != null) {
                        filteredPosts = filteredPosts.filter { it.category == category }
                    }
                    
                    // Filtrare după căutare
                    if (query.isNotBlank()) {
                        filteredPosts = filteredPosts.filter { post ->
                            post.title.contains(query, ignoreCase = true) ||
                            post.description.contains(query, ignoreCase = true)
                        }
                    }
                    
                    // Filtrare după preț
                    if (range.first != null || range.second != null) {
                        filteredPosts = filteredPosts.filter { post ->
                            val price = post.price
                            val minPrice = range.first ?: Double.MIN_VALUE
                            val maxPrice = range.second ?: Double.MAX_VALUE
                            price in minPrice..maxPrice
                        }
                    }
                    
                    filteredPosts
                }.collect { posts ->
                    _postsState.value = PostsState.Success(posts)
                }
            } catch (e: Exception) {
                _postsState.value = PostsState.Error("Eroare la încărcarea anunțurilor: ${e.message}")
            }
        }
    }
    
    /**
     * Încarcă rate-urile de schimb.
     */
    private fun loadExchangeRates() {
        viewModelScope.launch {
            try {
                _currencyState.value = CurrencyState.Loading
                exchangeRateRepository.getLatestRates("RON").collect { response ->
                    if (response.success) {
                        _exchangeRates.value = response.rates
                        _currencyState.value = CurrencyState.Success(response.rates)
                    } else {
                        _currencyState.value = CurrencyState.Error("Nu s-au putut încărca rate-urile de schimb")
                    }
                }
            } catch (e: Exception) {
                _currencyState.value = CurrencyState.Error("Eroare la încărcarea rate-urilor: ${e.message}")
            }
        }
    }
    
    /**
     * Convertește prețul unui anunț în moneda selectată.
     * 
     * @param post anunțul pentru care se convertește prețul
     * @return prețul convertit
     */
    fun convertPrice(post: Post): Double {
        val rates = _exchangeRates.value
        val targetCurrency = _selectedCurrency.value.code
        
        return if (targetCurrency == "RON") {
            post.price // Prețul original este în RON
        } else {
            exchangeRateRepository.convertWithRates(
                post.price, "RON", targetCurrency, rates
            )
        }
    }
    
    /**
     * Schimbă categoria selectată.
     * 
     * @param category noua categorie sau null pentru toate
     */
    fun setCategory(category: PostCategory?) {
        _selectedCategory.value = category
    }
    
    /**
     * Setează query-ul de căutare.
     * 
     * @param query textul de căutat
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Setează intervalul de preț.
     * 
     * @param minPrice prețul minim (opțional)
     * @param maxPrice prețul maxim (opțional)
     */
    fun setPriceRange(minPrice: Double?, maxPrice: Double?) {
        _priceRange.value = minPrice to maxPrice
    }
    
    /**
     * Schimbă moneda selectată.
     * 
     * @param currency noua monedă
     */
    fun setCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }
    
    /**
     * Resetează toate filtrele.
     */
    fun resetFilters() {
        _selectedCategory.value = null
        _searchQuery.value = ""
        _priceRange.value = null to null
    }
    
    /**
     * Reîmprospătează datele.
     */
    fun refresh() {
        loadPosts()
        loadExchangeRates()
    }
    
    /**
     * Obține toate monedele suportate.
     * 
     * @return lista cu monedele suportate
     */
    fun getSupportedCurrencies(): List<Currency> {
        return exchangeRateRepository.getSupportedCurrencies()
    }
}

/**
 * Stările pentru lista de anunțuri.
 */
sealed class PostsState {
    object Loading : PostsState()
    data class Success(val posts: List<Post>) : PostsState()
    data class Error(val message: String) : PostsState()
}

/**
 * Stările pentru conversia valutară.
 */
sealed class CurrencyState {
    object Idle : CurrencyState()
    object Loading : CurrencyState()
    data class Success(val rates: Map<String, Double>) : CurrencyState()
    data class Error(val message: String) : CurrencyState()
}
