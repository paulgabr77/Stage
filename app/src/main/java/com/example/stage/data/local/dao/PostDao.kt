package com.example.stage.data.local.dao

import androidx.room.*
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.PostCategory
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) pentru entitatea Post.
 * Definește toate operațiile pe care le putem face cu anunțurile în baza de date.
 */
@Dao
interface PostDao {
    
    /**
     * Inserează un anunț nou în baza de date.
     * @param post anunțul de inserat
     * @return ID-ul anunțului inserat
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post): Long
    
    /**
     * Actualizează un anunț existent.
     * @param post anunțul actualizat
     */
    @Update
    suspend fun updatePost(post: Post)
    
    /**
     * Șterge un anunț din baza de date.
     * @param post anunțul de șters
     */
    @Delete
    suspend fun deletePost(post: Post)
    
    /**
     * Obține un anunț după ID.
     * @param postId ID-ul anunțului
     * @return anunțul găsit sau null
     */
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?
    
    /**
     * Obține toate anunțurile ca Flow (pentru observare reactivă).
     * @return Flow cu toate anunțurile, ordonate după data creării
     */
    @Query("SELECT * FROM posts WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActivePosts(): Flow<List<Post>>
    
    /**
     * Obține anunțurile unui utilizator specific.
     * @param userId ID-ul utilizatorului
     * @return Flow cu anunțurile utilizatorului
     */
    @Query("SELECT * FROM posts WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC")
    fun getPostsByUserId(userId: Long): Flow<List<Post>>
    
    /**
     * Obține anunțurile după categorie (mașini sau piese).
     * @param category categoria anunțurilor
     * @return Flow cu anunțurile din categoria specificată
     */
    @Query("SELECT * FROM posts WHERE category = :category AND isActive = 1 ORDER BY createdAt DESC")
    fun getPostsByCategory(category: PostCategory): Flow<List<Post>>
    
    /**
     * Caută anunțuri după titlu sau descriere.
     * @param searchQuery textul de căutat
     * @return Flow cu anunțurile care conțin textul căutat
     */
    @Query("SELECT * FROM posts WHERE (title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND isActive = 1 ORDER BY createdAt DESC")
    fun searchPosts(searchQuery: String): Flow<List<Post>>
    
    /**
     * Obține anunțurile într-un interval de preț.
     * @param minPrice prețul minim
     * @param maxPrice prețul maxim
     * @return Flow cu anunțurile din intervalul de preț
     */
    @Query("SELECT * FROM posts WHERE price BETWEEN :minPrice AND :maxPrice AND isActive = 1 ORDER BY price ASC")
    fun getPostsByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<Post>>
    
    /**
     * Obține numărul total de anunțuri active.
     * @return numărul de anunțuri active
     */
    @Query("SELECT COUNT(*) FROM posts WHERE isActive = 1")
    suspend fun getActivePostsCount(): Int
    
    /**
     * Obține numărul de anunțuri pentru un utilizator.
     * @param userId ID-ul utilizatorului
     * @return numărul de anunțuri ale utilizatorului
     */
    @Query("SELECT COUNT(*) FROM posts WHERE userId = :userId AND isActive = 1")
    suspend fun getPostsCountByUserId(userId: Long): Int
    
    /**
     * Marchează un anunț ca fiind inactiv (ștergere logică).
     * @param postId ID-ul anunțului
     */
    @Query("UPDATE posts SET isActive = 0 WHERE id = :postId")
    suspend fun deactivatePost(postId: Long)
}

