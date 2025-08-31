package com.example.stage.data.repository

import com.example.stage.data.local.dao.UserDao
import com.example.stage.data.local.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository pentru operațiile cu utilizatori.
 * Combină datele locale (baza de date) cu logica de business.
 */
class UserRepository(
    private val userDao: UserDao
) {
    
    /**
     * Înregistrează un utilizator nou.
     * 
     * @param user utilizatorul de înregistrat
     * @return ID-ul utilizatorului creat sau -1 dacă emailul există deja
     */
    suspend fun registerUser(user: User): Long {
        println("DEBUG: UserRepository.registerUser called for email: ${user.email}")
        return if (!userDao.userExists(user.email)) {
            val userId = userDao.insertUser(user)
            println("DEBUG: User registered successfully with ID: $userId")
            userId
        } else {
            println("DEBUG: User registration failed - email already exists")
            -1L // Email-ul există deja
        }
    }
    
    /**
     * Autentifică un utilizator.
     * 
     * @param email emailul utilizatorului
     * @param password parola utilizatorului
     * @return utilizatorul dacă autentificarea reușește, null altfel
     */
    suspend fun loginUser(email: String, password: String): User? {
        println("DEBUG: UserRepository.loginUser called for email: $email")
        val user = userDao.getUserByEmail(email)
        println("DEBUG: User found in database: $user")
        return if (user?.password == password) {
            println("DEBUG: Login successful for user: ${user.name}")
            user
        } else {
            println("DEBUG: Login failed - invalid credentials")
            null
        }
    }
    
    /**
     * Obține un utilizator după ID.
     * 
     * @param userId ID-ul utilizatorului
     * @return utilizatorul găsit sau null
     */
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    /**
     * Actualizează datele unui utilizator.
     * 
     * @param user utilizatorul actualizat
     */
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    /**
     * Șterge un utilizator.
     * 
     * @param user utilizatorul de șters
     */
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
    
    /**
     * Verifică dacă un email există deja.
     * 
     * @param email emailul de verificat
     * @return true dacă emailul există, false altfel
     */
    suspend fun userExists(email: String): Boolean {
        return userDao.userExists(email)
    }
    
    /**
     * Obține toți utilizatorii ca Flow.
     * 
     * @return Flow cu toți utilizatorii
     */
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
    
    /**
     * Obține numărul total de utilizatori.
     * 
     * @return numărul de utilizatori
     */
    suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}
