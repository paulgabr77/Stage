package com.example.stage.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.stage.data.remote.dto.Currency
import com.example.stage.data.local.entities.PostCategory

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
        private const val KEY_SELECTED_CATEGORY = "selected_category"
    }
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

    fun saveUserSession(userId: Long, email: String, name: String) {
        sharedPreferences.edit()
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_NAME, name)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(KEY_USER_ID, -1L)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearUserSession() {
        sharedPreferences.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    fun saveSelectedCurrency(currency: Currency) {
        sharedPreferences.edit()
            .putString(KEY_SELECTED_CURRENCY, currency.code)
            .apply()
    }

    fun getSelectedCurrency(): Currency {
        val currencyCode = sharedPreferences.getString(KEY_SELECTED_CURRENCY, "RON")
        return Currency.values().find { it.code == currencyCode } ?: Currency.RON
    }

    fun saveDarkMode(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveLastVisitedPage(pageName: String) {
        sharedPreferences.edit()
            .putString(KEY_LAST_VISITED_PAGE, pageName)
            .apply()
    }

    fun getLastVisitedPage(): String? {
        return sharedPreferences.getString(KEY_LAST_VISITED_PAGE, null)
    }
    
    // ==================== APP SETTINGS ====================

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchCompleted() {
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }
    fun saveNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
            .apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    fun saveAutoRefreshEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_AUTO_REFRESH_ENABLED, enabled)
            .apply()
    }

    fun isAutoRefreshEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_REFRESH_ENABLED, true)
    }

    fun saveRefreshInterval(minutes: Int) {
        sharedPreferences.edit()
            .putInt(KEY_REFRESH_INTERVAL, minutes)
            .apply()
    }

    fun getRefreshInterval(): Int {
        return sharedPreferences.getInt(KEY_REFRESH_INTERVAL, 5) // 5 minute implicit
    }

    fun saveSelectedCategory(category: PostCategory?) {
        val categoryName = category?.name ?: "ALL"
        sharedPreferences.edit()
            .putString(KEY_SELECTED_CATEGORY, categoryName)
            .apply()
    }

    fun getSelectedCategory(): PostCategory? {
        val categoryName = sharedPreferences.getString(KEY_SELECTED_CATEGORY, "ALL")
        return if (categoryName == "ALL") {
            null
        } else {
            PostCategory.values().find { it.name == categoryName }
        }
    }

    fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun getAllPreferences(): Map<String, *> {
        return sharedPreferences.all
    }
}
