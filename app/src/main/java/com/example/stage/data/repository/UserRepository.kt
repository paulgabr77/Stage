package com.example.stage.data.repository

import com.example.stage.data.local.dao.UserDao
import com.example.stage.data.local.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {

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

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun userExists(email: String): Boolean {
        return userDao.userExists(email)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}
