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
    
    init {
        println("DEBUG: AuthViewModel created successfully with UserRepository")
    }
    
    // State pentru login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    // State pentru register
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()
    
    // Utilizatorul curent
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    // Date pentru păstrarea între ecrane
    private val _sharedEmail = MutableStateFlow("")
    val sharedEmail: StateFlow<String> = _sharedEmail.asStateFlow()
    
    private val _sharedPassword = MutableStateFlow("")
    val sharedPassword: StateFlow<String> = _sharedPassword.asStateFlow()
    
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
                println("DEBUG: Attempting login with email: $email")
                val user = userRepository.loginUser(email, password)
                
                if (user != null) {
                    println("DEBUG: Login successful for user: ${user.name}")
                    _currentUser.value = user
                    _loginState.value = LoginState.Success(user)
                } else {
                    println("DEBUG: Login failed - invalid credentials")
                    _loginState.value = LoginState.Error("Email sau parolă incorectă")
                }
            } catch (e: Exception) {
                println("DEBUG: Login exception: ${e.message}")
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
        println("DEBUG: AuthViewModel.register called with name=$name, email=$email")
        
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            println("DEBUG: Validation failed - blank fields")
            _registerState.value = RegisterState.Error("Toate câmpurile sunt obligatorii")
            return
        }
        
        if (password.length < 6) {
            println("DEBUG: Validation failed - password too short")
            _registerState.value = RegisterState.Error("Parola trebuie să aibă cel puțin 6 caractere")
            return
        }
        
        _registerState.value = RegisterState.Loading
        
        viewModelScope.launch {
            try {
                println("DEBUG: Starting register operation")
                
                val user = User(
                    email = email,
                    password = password,
                    name = name,
                    phone = phone
                )
                
                val userId = userRepository.registerUser(user)
                
                if (userId != -1L) {
                    val newUser = user.copy(id = userId)
                    println("DEBUG: User registered successfully with ID: $userId")
                    _currentUser.value = newUser
                    _registerState.value = RegisterState.Success(newUser)
                } else {
                    println("DEBUG: Registration failed - email already exists")
                    _registerState.value = RegisterState.Error("Email-ul există deja")
                }
            } catch (e: Exception) {
                println("DEBUG: Registration exception: ${e.message}")
                _registerState.value = RegisterState.Error("Eroare la înregistrare: ${e.message}")
            }
        }
    }
    
    /**
     * Setează emailul partajat între ecrane.
     */
    fun setSharedEmail(email: String) {
        _sharedEmail.value = email
    }
    
    /**
     * Setează parola partajată între ecrane.
     */
    fun setSharedPassword(password: String) {
        _sharedPassword.value = password
    }
    
    /**
     * Obține emailul partajat.
     */
    fun getSharedEmail(): String {
        return _sharedEmail.value
    }
    
    /**
     * Obține parola partajată.
     */
    fun getSharedPassword(): String {
        return _sharedPassword.value
    }
    
    /**
     * Șterge datele partajate (la logout).
     */
    fun clearSharedData() {
        _sharedEmail.value = ""
        _sharedPassword.value = ""
    }
    
    /**
     * Deconectează utilizatorul curent.
     */
    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
        clearSharedData()
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
