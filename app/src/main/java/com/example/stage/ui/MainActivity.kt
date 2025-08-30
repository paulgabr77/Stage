package com.example.stage.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stage.ui.addpost.AddPostScreen
import com.example.stage.ui.auth.LoginScreen
import com.example.stage.ui.auth.RegisterScreen
import com.example.stage.ui.home.HomeScreen
import com.example.stage.ui.profile.ProfileScreen
import com.example.stage.ui.theme.StageTheme
import com.example.stage.utils.Constants
import com.example.stage.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StageApp()
                }
            }
        }
    }
}

/**
 * Aplicația principală cu navigare și ViewModels.
 */
@Composable
fun StageApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // ViewModels
    val authViewModel: AuthViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = Constants.Routes.LOGIN
    ) {
        // Login Screen
        composable(Constants.Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Constants.Routes.HOME) {
                        popUpTo(Constants.Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Constants.Routes.REGISTER)
                },
                onShowError = { errorMessage ->
                    // TODO: Show error message (Snackbar, Toast, etc.)
                    println("Error: $errorMessage")
                },
                viewModel = authViewModel
            )
        }
        
        // Register Screen
        composable(Constants.Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Constants.Routes.HOME) {
                        popUpTo(Constants.Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onShowError = { errorMessage ->
                    // TODO: Show error message
                    println("Error: $errorMessage")
                    // Adaug Toast pentru a vedea erorile
                    android.widget.Toast.makeText(
                        context,
                        errorMessage,
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                },
                viewModel = authViewModel
            )
        }
        
        // Home Screen
        composable(Constants.Routes.HOME) {
            HomeScreen(
                posts = emptyList(), // TODO: Get from ViewModel
                selectedCurrency = com.example.stage.data.remote.dto.Currency.RON,
                isLoading = false,
                onPostClick = { post ->
                    // TODO: Navigate to post details
                    println("Clicked on post: ${post.title}")
                },
                onAddPostClick = {
                    navController.navigate(Constants.Routes.ADD_POST)
                },
                onProfileClick = {
                    navController.navigate(Constants.Routes.PROFILE)
                },
                onCurrencyChange = { currency ->
                    // TODO: Update currency in ViewModel
                    println("Currency changed to: ${currency.displayName}")
                },
                onCategoryFilter = { category ->
                    // TODO: Filter posts by category
                    println("Filter by category: ${category?.displayName}")
                },
                onSearchQuery = { query ->
                    // TODO: Search posts
                    println("Search query: $query")
                }
            )
        }
        
        // Add Post Screen
        composable(Constants.Routes.ADD_POST) {
            AddPostScreen(
                onPostCreated = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onShowError = { errorMessage ->
                    // TODO: Show error message
                    println("Error: $errorMessage")
                }
            )
        }
        
        // Profile Screen
        composable(Constants.Routes.PROFILE) {
            ProfileScreen(
                user = null, // TODO: Get from ViewModel
                userPosts = emptyList(), // TODO: Get from ViewModel
                isLoading = false,
                onEditProfile = {
                    // TODO: Navigate to edit profile
                    println("Edit profile clicked")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Constants.Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPostClick = { post ->
                    // TODO: Navigate to post details
                    println("Clicked on user post: ${post.title}")
                },
                onDeletePost = { post ->
                    // TODO: Delete post
                    println("Delete post: ${post.title}")
                },
                onDeactivatePost = { postId ->
                    // TODO: Deactivate post
                    println("Deactivate post: $postId")
                }
            )
        }
    }
}