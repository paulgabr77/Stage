package com.example.stage.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.User
import com.example.stage.utils.Constants
import com.example.stage.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    userPosts: List<Post> = emptyList(),
    isLoading: Boolean = false,
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPostClick: (Post) -> Unit = {},
    onDeletePost: (Post) -> Unit = {},
    onDeactivatePost: (Long) -> Unit = {}
) {
    // Collect current user from AuthViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Deconectare"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(Constants.DEFAULT_PADDING.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // User info section
                item {
                    UserInfoCard(
                        user = currentUser,
                        onEditProfile = onEditProfile
                    )
                }
                
                // Statistics section
                item {
                    StatisticsCard(userPosts = userPosts)
                }
                
                // User posts section
                item {
                    Text(
                        text = "Anunțurile mele",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                if (userPosts.isEmpty()) {
                    item {
                        EmptyPostsCard()
                    }
                } else {
                    items(userPosts) { post ->
                        UserPostCard(
                            post = post,
                            onClick = { onPostClick(post) },
                            onDelete = { onDeletePost(post) },
                            onDeactivate = { onDeactivatePost(post.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoCard(
    user: User?,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User avatar and info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar placeholder
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = user?.name ?: "Utilizator",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Text(
                            text = user?.email ?: "email@example.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (!user?.phone.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Telefon",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user?.phone ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // Data înregistrării (dacă avem ID, înseamnă că e înregistrat)
                        if (user?.id != null && user.id > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Membru din",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Membru din ${formatRegistrationDate(user.id)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                // Edit button
                IconButton(onClick = onEditProfile) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editează profil"
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsCard(userPosts: List<Post>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
        ) {
            Text(
                text = "Statistici",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    title = "Total anunțuri",
                    value = userPosts.size.toString(),
                    icon = Icons.Default.Star
                )
                
                StatCard(
                    title = "Active",
                    value = userPosts.count { it.isActive }.toString(),
                    icon = Icons.Default.Star
                )
                
                StatCard(
                    title = "Inactive",
                    value = userPosts.count { !it.isActive }.toString(),
                    icon = Icons.Default.Star
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPostCard(
    post: Post,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onDeactivate: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
            ) {
            // Header with title and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                // Status chip
                Surface(
                    color = if (post.isActive) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (post.isActive) "Activ" else "Inactiv",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Description
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Footer with price and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${post.price.toInt()} lei",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row {
                    // Deactivate/Activate button
                    IconButton(
                        onClick = onDeactivate
                    ) {
                        Icon(
                            imageVector = if (post.isActive) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = if (post.isActive) "Dezactivează" else "Activează"
                        )
                    }
                    
                    // Delete button
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Șterge",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyPostsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.DEFAULT_PADDING.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Nu sunt anunțuri",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Nu ai încă anunțuri",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Creează primul tău anunț pentru a începe!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatRegistrationDate(userId: Long): String {
    // Pentru simplitate, folosim ID-ul pentru a genera o dată aproximativă
    val currentYear = 2022
    val month = ((userId % 12) + 1).toInt()
    val monthName = when (month) {
        1 -> "Ianuarie"
        2 -> "Februarie"
        3 -> "Martie"
        4 -> "Aprilie"
        5 -> "Mai"
        6 -> "Iunie"
        7 -> "Iulie"
        8 -> "August"
        9 -> "Septembrie"
        10 -> "Octombrie"
        11 -> "Noiembrie"
        12 -> "Decembrie"
        else -> "Ianuarie"
    }
    
    return "$monthName $currentYear"
}

