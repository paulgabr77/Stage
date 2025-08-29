package com.example.stage.data.local.dao

import androidx.room.*
import com.example.stage.data.local.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) pentru entitatea User.
 * Definește toate operațiile pe care le putem face cu utilizatorii în baza de date.
 */
@Dao
interface UserDao {
    
    /**
     * Inserează un utilizator nou în baza de date.
     * @param user utilizatorul de inserat
     * @return ID-ul utilizatorului inserat
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
    
    /**
     * Actualizează un utilizator existent.
     * @param user utilizatorul actualizat
     */
    @Update
    suspend fun updateUser(user: User)
    
    /**
     * Șterge un utilizator din baza de date.
     * @param user utilizatorul de șters
     */
    @Delete
    suspend fun deleteUser(user: User)
    
    /**
     * Obține un utilizator după ID.
     * @param userId ID-ul utilizatorului
     * @return utilizatorul găsit sau null
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?
    
    /**
     * Obține un utilizator după email (pentru login).
     * @param email emailul utilizatorului
     * @return utilizatorul găsit sau null
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    /**
     * Verifică dacă există un utilizator cu emailul dat.
     * @param email emailul de verificat
     * @return true dacă există, false altfel
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun userExists(email: String): Boolean
    
    /**
     * Obține toți utilizatorii ca Flow (pentru observare reactivă).
     * @return Flow cu toți utilizatorii
     */
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<User>>
    
    /**
     * Obține numărul total de utilizatori.
     * @return numărul de utilizatori
     */
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}

