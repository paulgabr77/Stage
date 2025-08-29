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
        return if (!userDao.userExists(user.email)) {
            userDao.insertUser(user)
        } else {
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
        val user = userDao.getUserByEmail(email)
        return if (user?.password == password) user else null
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
