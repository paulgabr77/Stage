package com.example.stage.data.local.dao

import androidx.room.*
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.PostCategory
import kotlinx.coroutines.flow.Flow

//Definește toate operațiile pe care le putem face cu anunțurile în baza de date.
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post): Long

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?

    @Query("SELECT * FROM posts WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActivePosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC")
    fun getPostsByUserId(userId: Long): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE category = :category AND isActive = 1 ORDER BY createdAt DESC")
    fun getPostsByCategory(category: PostCategory): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE (title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND isActive = 1 ORDER BY createdAt DESC")
    fun searchPosts(searchQuery: String): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE price BETWEEN :minPrice AND :maxPrice AND isActive = 1 ORDER BY price ASC")
    fun getPostsByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<Post>>

    @Query("SELECT COUNT(*) FROM posts WHERE isActive = 1")
    suspend fun getActivePostsCount(): Int

    @Query("SELECT COUNT(*) FROM posts WHERE userId = :userId AND isActive = 1")
    suspend fun getPostsCountByUserId(userId: Long): Int

    @Query("UPDATE posts SET isActive = 0 WHERE id = :postId")
    suspend fun deactivatePost(postId: Long)
}

