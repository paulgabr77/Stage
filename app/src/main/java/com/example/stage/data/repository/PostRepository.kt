package com.example.stage.data.repository

import com.example.stage.data.local.dao.PostDao
import com.example.stage.data.local.dao.CarDetailsDao
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.CarDetails
import com.example.stage.data.local.entities.PostCategory
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val postDao: PostDao,
    private val carDetailsDao: CarDetailsDao
) {

    suspend fun createPost(post: Post, carDetails: CarDetails? = null): Long {
        val postId = postDao.insertPost(post)
        
        // Dacă este un anunț pentru mașină și avem detalii, le salvăm
        if (post.category == PostCategory.CAR && carDetails != null) {
            carDetailsDao.insertCarDetails(carDetails.copy(postId = postId))
        }
        
        return postId
    }

    fun getAllActivePosts(): Flow<List<Post>> {
        return postDao.getAllActivePosts()
    }

    fun getPostsByCategory(category: PostCategory): Flow<List<Post>> {
        return postDao.getPostsByCategory(category)
    }

    fun getPostsByUserId(userId: Long): Flow<List<Post>> {
        return postDao.getPostsByUserId(userId)
    }

    fun searchPosts(searchQuery: String): Flow<List<Post>> {
        return postDao.searchPosts(searchQuery)
    }

    fun getPostsByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<Post>> {
        return postDao.getPostsByPriceRange(minPrice, maxPrice)
    }

    suspend fun getPostById(postId: Long): Post? {
        return postDao.getPostById(postId)
    }

    suspend fun getCarDetailsForPost(postId: Long): CarDetails? {
        return carDetailsDao.getCarDetailsByPostId(postId)
    }

    suspend fun updatePost(post: Post, carDetails: CarDetails? = null) {
        postDao.updatePost(post)
        
        if (post.category == PostCategory.CAR && carDetails != null) {
            carDetailsDao.updateCarDetails(carDetails)
        }
    }

    suspend fun deactivatePost(postId: Long) {
        postDao.deactivatePost(postId)
    }

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

    suspend fun getPostsCountByUserId(userId: Long): Int {
        return postDao.getPostsCountByUserId(userId)
    }

    suspend fun getActivePostsCount(): Int {
        return postDao.getActivePostsCount()
    }
}
