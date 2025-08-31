package com.example.stage.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.stage.utils.DependencyProvider
import com.example.stage.viewmodel.AuthViewModel
import com.example.stage.viewmodel.AuthViewModelFactory
import com.example.stage.viewmodel.HomeViewModel
import com.example.stage.viewmodel.HomeViewModelFactory
import com.example.stage.viewmodel.AddPostViewModel
import com.example.stage.viewmodel.AddPostViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inițializează dependențele
        DependencyProvider.initialize(this)
        
        setContent {
            StageTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF667eea),
                                    Color(0xFF764ba2),
                                    Color(0xFFf093fb)
                                )
                            )
                        ),
                    color = Color.Transparent
                ) {
                    StageApp()
                }
            }
        }
    }
}

@Composable
fun StageApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Obține Repository-uri din DependencyProvider
    val userRepository = DependencyProvider.getUserRepository()
    val postRepository = DependencyProvider.getPostRepository()
    val exchangeRateRepository = DependencyProvider.getExchangeRateRepository()
    val sharedPreferencesManager = DependencyProvider.getSharedPreferencesManager()
    
    // Creează ViewModelFactory-uri
    val authViewModelFactory = AuthViewModelFactory(userRepository)
    val homeViewModelFactory = HomeViewModelFactory(postRepository, exchangeRateRepository, sharedPreferencesManager)
    val addPostViewModelFactory = AddPostViewModelFactory(postRepository)
    
    // ViewModels - folosim factory-uri pentru injectarea dependențelor
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
    val addPostViewModel: AddPostViewModel = viewModel(factory = addPostViewModelFactory)
    
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
                homeViewModel = homeViewModel,
                onPostClick = { post ->
                    println("Clicked on post: ${post.title}")
                },
                onAddPostClick = {
                    navController.navigate(Constants.Routes.ADD_POST)
                },
                onProfileClick = {
                    navController.navigate(Constants.Routes.PROFILE)
                }
            )
        }
        
        // Add Post Screen
        composable(Constants.Routes.ADD_POST) {
            AddPostScreen(
                addPostViewModel = addPostViewModel,
                onPostCreated = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onShowError = { errorMessage ->
                    println("Error: $errorMessage")
                }
            )
        }
        
        // Profile Screen
        composable(Constants.Routes.PROFILE) {
            ProfileScreen(
                authViewModel = authViewModel,
                userPosts = emptyList(),
                isLoading = false,
                onEditProfile = {
                    println("Edit profile clicked")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Constants.Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPostClick = { post ->
                    println("Clicked on user post: ${post.title}")
                },
                onDeletePost = { post ->
                    println("Delete post: ${post.title}")
                },
                onDeactivatePost = { postId ->
                    println("Deactivate post: $postId")
                }
            )
        }
    }
}