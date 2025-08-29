package com.example.stage.data.repository

import com.example.stage.data.local.dao.PostDao
import com.example.stage.data.local.dao.CarDetailsDao
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.CarDetails
import com.example.stage.data.local.entities.PostCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository pentru operațiile cu anunțuri.
 * Combină datele locale (baza de date) cu logica de business.
 */
class PostRepository(
    private val postDao: PostDao,
    private val carDetailsDao: CarDetailsDao
) {
    
    /**
     * Creează un anunț nou.
     * 
     * @param post anunțul de creat
     * @param carDetails detaliile mașinii (opțional, doar pentru anunțuri de tip CAR)
     * @return ID-ul anunțului creat
     */
    suspend fun createPost(post: Post, carDetails: CarDetails? = null): Long {
        val postId = postDao.insertPost(post)
        
        // Dacă este un anunț pentru mașină și avem detalii, le salvăm
        if (post.category == PostCategory.CAR && carDetails != null) {
            carDetailsDao.insertCarDetails(carDetails.copy(postId = postId))
        }
        
        return postId
    }
    
    /**
     * Obține toate anunțurile active ca Flow.
     * 
     * @return Flow cu toate anunțurile active
     */
    fun getAllActivePosts(): Flow<List<Post>> {
        return postDao.getAllActivePosts()
    }
    
    /**
     * Obține anunțurile după categorie.
     * 
     * @param category categoria anunțurilor
     * @return Flow cu anunțurile din categoria specificată
     */
    fun getPostsByCategory(category: PostCategory): Flow<List<Post>> {
        return postDao.getPostsByCategory(category)
    }
    
    /**
     * Obține anunțurile unui utilizator.
     * 
     * @param userId ID-ul utilizatorului
     * @return Flow cu anunțurile utilizatorului
     */
    fun getPostsByUserId(userId: Long): Flow<List<Post>> {
        return postDao.getPostsByUserId(userId)
    }
    
    /**
     * Caută anunțuri după text.
     * 
     * @param searchQuery textul de căutat
     * @return Flow cu anunțurile care conțin textul căutat
     */
    fun searchPosts(searchQuery: String): Flow<List<Post>> {
        return postDao.searchPosts(searchQuery)
    }
    
    /**
     * Obține anunțurile într-un interval de preț.
     * 
     * @param minPrice prețul minim
     * @param maxPrice prețul maxim
     * @return Flow cu anunțurile din intervalul de preț
     */
    fun getPostsByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<Post>> {
        return postDao.getPostsByPriceRange(minPrice, maxPrice)
    }
    
    /**
     * Obține un anunț după ID.
     * 
     * @param postId ID-ul anunțului
     * @return anunțul găsit sau null
     */
    suspend fun getPostById(postId: Long): Post? {
        return postDao.getPostById(postId)
    }
    
    /**
     * Obține detaliile unei mașini pentru un anunț.
     * 
     * @param postId ID-ul anunțului
     * @return detaliile mașinii sau null
     */
    suspend fun getCarDetailsForPost(postId: Long): CarDetails? {
        return carDetailsDao.getCarDetailsByPostId(postId)
    }
    
    /**
     * Actualizează un anunț.
     * 
     * @param post anunțul actualizat
     * @param carDetails detaliile mașinii actualizate (opțional)
     */
    suspend fun updatePost(post: Post, carDetails: CarDetails? = null) {
        postDao.updatePost(post)
        
        if (post.category == PostCategory.CAR && carDetails != null) {
            carDetailsDao.updateCarDetails(carDetails)
        }
    }
    
    /**
     * Marchează un anunț ca fiind inactiv (ștergere logică).
     * 
     * @param postId ID-ul anunțului
     */
    suspend fun deactivatePost(postId: Long) {
        postDao.deactivatePost(postId)
    }
    
    /**
     * Șterge complet un anunț din baza de date.
     * 
     * @param post anunțul de șters
     */
    suspend fun deletePost(post: Post) {
        // Șterge și detaliile mașinii dacă există
        if (post.category == PostCategory.CAR) {
            val carDetails = carDetailsDao.getCarDetailsByPostId(post.id)
            if (carDetails != null) {
                carDetailsDao.deleteCarDetails(carDetails)
            }
        }
        
        postDao.deletePost(post)
    }
    
    /**
     * Obține numărul de anunțuri pentru un utilizator.
     * 
     * @param userId ID-ul utilizatorului
     * @return numărul de anunțuri
     */
    suspend fun getPostsCountByUserId(userId: Long): Int {
        return postDao.getPostsCountByUserId(userId)
    }
    
    /**
     * Obține numărul total de anunțuri active.
     * 
     * @return numărul de anunțuri active
     */
    suspend fun getActivePostsCount(): Int {
        return postDao.getActivePostsCount()
    }
}
