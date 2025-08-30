package com.example.stage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stage.data.local.entities.User
import com.example.stage.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel pentru ecranele de autentificare (login și register).
 * Gestionează starea UI-ului și logica de business pentru autentificare.
 */
class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    // State pentru login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    // State pentru register
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()
    
    // Utilizatorul curent
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    /**
     * Autentifică un utilizator.
     * 
     * @param email emailul utilizatorului
     * @param password parola utilizatorului
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email și parola sunt obligatorii")
            return
        }
        
        _loginState.value = LoginState.Loading
        
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _loginState.value = LoginState.Success(user)
                } else {
                    _loginState.value = LoginState.Error("Email sau parolă incorectă")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Eroare la autentificare: ${e.message}")
            }
        }
    }
    
    /**
     * Înregistrează un utilizator nou.
     * 
     * @param name numele utilizatorului
     * @param email emailul utilizatorului
     * @param password parola utilizatorului
     * @param phone telefonul utilizatorului (opțional)
     */
    fun register(name: String, email: String, password: String, phone: String? = null) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState.Error("Toate câmpurile sunt obligatorii")
            return
        }
        
        if (password.length < 6) {
            _registerState.value = RegisterState.Error("Parola trebuie să aibă cel puțin 6 caractere")
            return
        }
        
        _registerState.value = RegisterState.Loading
        
        viewModelScope.launch {
            try {
                val user = User(
                    email = email,
                    password = password,
                    name = name,
                    phone = phone
                )
                
                val userId = userRepository.registerUser(user)
                if (userId != -1L) {
                    val newUser = user.copy(id = userId)
                    _currentUser.value = newUser
                    _registerState.value = RegisterState.Success(newUser)
                } else {
                    _registerState.value = RegisterState.Error("Email-ul există deja")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Eroare la înregistrare: ${e.message}")
            }
        }
    }
    
    /**
     * Deconectează utilizatorul curent.
     */
    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
    }
    
    /**
     * Resetează starea de login.
     */
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
    
    /**
     * Resetează starea de register.
     */
    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }
    
    /**
     * Verifică dacă un email există deja.
     * 
     * @param email emailul de verificat
     * @return true dacă emailul există, false altfel
     */
    fun checkEmailExists(email: String): Boolean {
        return viewModelScope.launch {
            userRepository.userExists(email)
        }.isCompleted
    }
}

/**
 * Stările pentru procesul de login.
 */
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

/**
 * Stările pentru procesul de register.
 */
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
