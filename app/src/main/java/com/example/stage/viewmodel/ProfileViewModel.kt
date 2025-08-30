package com.example.stage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stage.data.local.entities.User
import com.example.stage.data.local.entities.Post
import com.example.stage.data.repository.UserRepository
import com.example.stage.data.repository.PostRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pentru ecranul de profil.
 * Gestionează datele utilizatorului și anunțurile sale.
 */
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    
    // State pentru profil
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()
    
    // State pentru anunțurile utilizatorului
    private val _userPostsState = MutableStateFlow<UserPostsState>(UserPostsState.Loading)
    val userPostsState: StateFlow<UserPostsState> = _userPostsState.asStateFlow()
    
    // State pentru actualizarea profilului
    private val _updateProfileState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateProfileState: StateFlow<UpdateProfileState> = _updateProfileState.asStateFlow()
    
    // Utilizatorul curent
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    // Anunțurile utilizatorului
    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts.asStateFlow()
    
    // Statistici
    private val _stats = MutableStateFlow<UserStats?>(null)
    val stats: StateFlow<UserStats?> = _stats.asStateFlow()
    
    /**
     * Încarcă profilul utilizatorului.
     * 
     * @param userId ID-ul utilizatorului
     */
    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileState.Loading
                
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    _currentUser.value = user
                    _profileState.value = ProfileState.Success(user)
                    loadUserPosts(userId)
                    loadUserStats(userId)
                } else {
                    _profileState.value = ProfileState.Error("Utilizatorul nu a fost găsit")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error("Eroare la încărcarea profilului: ${e.message}")
            }
        }
    }
    
    /**
     * Încarcă anunțurile utilizatorului.
     * 
     * @param userId ID-ul utilizatorului
     */
    private fun loadUserPosts(userId: Long) {
        viewModelScope.launch {
            try {
                _userPostsState.value = UserPostsState.Loading
                
                postRepository.getPostsByUserId(userId).collect { posts ->
                    _userPosts.value = posts
                    _userPostsState.value = UserPostsState.Success(posts)
                }
            } catch (e: Exception) {
                _userPostsState.value = UserPostsState.Error("Eroare la încărcarea anunțurilor: ${e.message}")
            }
        }
    }
    
    /**
     * Încarcă statisticile utilizatorului.
     * 
     * @param userId ID-ul utilizatorului
     */
    private fun loadUserStats(userId: Long) {
        viewModelScope.launch {
            try {
                val postsCount = postRepository.getPostsCountByUserId(userId)
                val activePosts = _userPosts.value.count { it.isActive }
                val inactivePosts = postsCount - activePosts
                
                _stats.value = UserStats(
                    totalPosts = postsCount,
                    activePosts = activePosts,
                    inactivePosts = inactivePosts
                )
            } catch (e: Exception) {
                // Ignorăm eroarea pentru statistici
            }
        }
    }
    
    /**
     * Actualizează profilul utilizatorului.
     * 
     * @param name numele nou
     * @param phone telefonul nou
     * @param profileImage imaginea de profil nouă
     */
    fun updateProfile(name: String, phone: String?, profileImage: String?) {
        val currentUser = _currentUser.value ?: return
        
        if (name.isBlank()) {
            _updateProfileState.value = UpdateProfileState.Error("Numele este obligatoriu")
            return
        }
        
        _updateProfileState.value = UpdateProfileState.Loading
        
        viewModelScope.launch {
            try {
                val updatedUser = currentUser.copy(
                    name = name.trim(),
                    phone = phone?.trim()?.takeIf { it.isNotBlank() },
                    profileImage = profileImage?.trim()?.takeIf { it.isNotBlank() }
                )
                
                userRepository.updateUser(updatedUser)
                _currentUser.value = updatedUser
                _updateProfileState.value = UpdateProfileState.Success(updatedUser)
                
            } catch (e: Exception) {
                _updateProfileState.value = UpdateProfileState.Error("Eroare la actualizarea profilului: ${e.message}")
            }
        }
    }
    
    /**
     * Șterge un anunț al utilizatorului.
     * 
     * @param post anunțul de șters
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(post)
                // Anunțurile se vor actualiza automat prin Flow
            } catch (e: Exception) {
                // Gestionăm eroarea în UI
            }
        }
    }
    
    /**
     * Marchează un anunț ca fiind inactiv.
     * 
     * @param postId ID-ul anunțului
     */
    fun deactivatePost(postId: Long) {
        viewModelScope.launch {
            try {
                postRepository.deactivatePost(postId)
                // Anunțurile se vor actualiza automat prin Flow
            } catch (e: Exception) {
                // Gestionăm eroarea în UI
            }
        }
    }
    
    /**
     * Resetează starea de actualizare profil.
     */
    fun resetUpdateState() {
        _updateProfileState.value = UpdateProfileState.Idle
    }
    
    /**
     * Reîmprospătează datele.
     * 
     * @param userId ID-ul utilizatorului
     */
    fun refresh(userId: Long) {
        loadProfile(userId)
    }
}

/**
 * Stările pentru profil.
 */
sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

/**
 * Stările pentru anunțurile utilizatorului.
 */
sealed class UserPostsState {
    object Loading : UserPostsState()
    data class Success(val posts: List<Post>) : UserPostsState()
    data class Error(val message: String) : UserPostsState()
}

/**
 * Stările pentru actualizarea profilului.
 */
sealed class UpdateProfileState {
    object Idle : UpdateProfileState()
    object Loading : UpdateProfileState()
    data class Success(val user: User) : UpdateProfileState()
    data class Error(val message: String) : UpdateProfileState()
}

/**
 * Statisticile utilizatorului.
 */
data class UserStats(
    val totalPosts: Int,
    val activePosts: Int,
    val inactivePosts: Int
)
