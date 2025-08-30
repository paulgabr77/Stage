package com.example.stage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitatea Post reprezinta un anunt in aplicatie (masina sau piesa).
 * Fiecare anunt apartine unui utilizator si poate fi de tip masina sau piesa.
 */
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,  // ID-ul utilizatorului care a creat anunțul
    val title: String,
    val description: String,
    val price: Double,
    val category: PostCategory,
    val images: List<String> = emptyList(),  // Lista de URL-uri pentru imagini
    val location: String? = null,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val isActive: Boolean = true,  // Daca anuntul este activ sau nu
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
enum class PostCategory {
    CAR,
    PARTS;
    
    val displayName: String
        get() = when (this) {
            CAR -> "Mașini"
            PARTS -> "Piese"
        }
}
