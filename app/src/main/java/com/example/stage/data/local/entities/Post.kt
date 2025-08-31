package com.example.stage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entitatea Post reprezinta un anunt in aplicatie (masina sau piesa).
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String,
    val price: Double,
    val category: PostCategory,
    val images: List<String> = emptyList(),
    val location: String? = null,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val isActive: Boolean = true,
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
