package com.example.stage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitatea User reprezinta un utilizator in aplicatie.
 * Aceasta clasa va fi convertita automat intr-un tabel SQLite de catre Room.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val phone: String? = null,
    val profileImage: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
