package com.example.stage.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.stage.data.remote.dto.Currency

/**
 * Manager pentru gestionarea preferințelor utilizatorului.
 * Salvează și încarcă setările aplicației folosind SharedPreferences.
 */
class SharedPreferencesManager(context: Context) {
    
    companion object {
        private const val PREF_NAME = "stage_preferences"
        
        // Chei pentru preferințe
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_SELECTED_CURRENCY = "selected_currency"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LAST_VISITED_PAGE = "last_visited_page"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_AUTO_REFRESH_ENABLED = "auto_refresh_enabled"
        private const val KEY_REFRESH_INTERVAL = "refresh_interval"
    }
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )
    
    // ==================== SESSION MANAGEMENT ====================
    
    /**
     * Salvează datele utilizatorului logat.
     */
    fun saveUserSession(userId: Long, email: String, name: String) {
        sharedPreferences.edit()
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_NAME, name)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    /**
     * Obține ID-ul utilizatorului logat.
     */
    fun getUserId(): Long {
        return sharedPreferences.getLong(KEY_USER_ID, -1L)
    }
    
    /**
     * Obține emailul utilizatorului logat.
     */
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Obține numele utilizatorului logat.
     */
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Verifică dacă utilizatorul este logat.
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Șterge sesiunea utilizatorului (logout).
     */
    fun clearUserSession() {
        sharedPreferences.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
    
    // ==================== CURRENCY PREFERENCES ====================
    
    /**
     * Salvează moneda selectată.
     */
    fun saveSelectedCurrency(currency: Currency) {
        sharedPreferences.edit()
            .putString(KEY_SELECTED_CURRENCY, currency.code)
            .apply()
    }
    
    /**
     * Obține moneda selectată.
     */
    fun getSelectedCurrency(): Currency {
        val currencyCode = sharedPreferences.getString(KEY_SELECTED_CURRENCY, "RON")
        return Currency.values().find { it.code == currencyCode } ?: Currency.RON
    }
    
    // ==================== THEME PREFERENCES ====================
    
    /**
     * Salvează preferința pentru tema dark/light.
     */
    fun saveDarkMode(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }
    
    /**
     * Verifică dacă tema dark este activată.
     */
    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }
    
    // ==================== NAVIGATION PREFERENCES ====================
    
    /**
     * Salvează ultima pagină vizitată.
     */
    fun saveLastVisitedPage(pageName: String) {
        sharedPreferences.edit()
            .putString(KEY_LAST_VISITED_PAGE, pageName)
            .apply()
    }
    
    /**
     * Obține ultima pagină vizitată.
     */
    fun getLastVisitedPage(): String? {
        return sharedPreferences.getString(KEY_LAST_VISITED_PAGE, null)
    }
    
    // ==================== APP SETTINGS ====================
    
    /**
     * Verifică dacă este prima lansare a aplicației.
     */
    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }
    
    /**
     * Marchează că aplicația a fost lansată pentru prima dată.
     */
    fun setFirstLaunchCompleted() {
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }
    
    /**
     * Salvează preferința pentru notificări.
     */
    fun saveNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
            .apply()
    }
    
    /**
     * Verifică dacă notificările sunt activate.
     */
    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }
    
    /**
     * Salvează preferința pentru auto-refresh.
     */
    fun saveAutoRefreshEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_AUTO_REFRESH_ENABLED, enabled)
            .apply()
    }
    
    /**
     * Verifică dacă auto-refresh-ul este activat.
     */
    fun isAutoRefreshEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_REFRESH_ENABLED, true)
    }
    
    /**
     * Salvează intervalul de refresh (în minute).
     */
    fun saveRefreshInterval(minutes: Int) {
        sharedPreferences.edit()
            .putInt(KEY_REFRESH_INTERVAL, minutes)
            .apply()
    }
    
    /**
     * Obține intervalul de refresh (în minute).
     */
    fun getRefreshInterval(): Int {
        return sharedPreferences.getInt(KEY_REFRESH_INTERVAL, 5) // 5 minute implicit
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Șterge toate preferințele (reset complet).
     */
    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }
    
    /**
     * Verifică dacă o cheie există în preferințe.
     */
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    
    /**
     * Obține toate preferințele ca mapă.
     */
    fun getAllPreferences(): Map<String, *> {
        return sharedPreferences.all
    }
}
