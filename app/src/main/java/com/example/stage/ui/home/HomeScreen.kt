package com.example.stage.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.stage.data.local.entities.PostCategory
import com.example.stage.data.remote.dto.Currency
import com.example.stage.utils.Constants
import com.example.stage.viewmodel.HomeViewModel
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onPostClick: (Post) -> Unit = {},
    onAddPostClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    // Collect state from ViewModel
    val postsState by homeViewModel.postsState.collectAsState()
    val selectedCurrency by homeViewModel.selectedCurrency.collectAsState()
    val selectedCategory by homeViewModel.selectedCategory.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    
    var showFilters by remember { mutableStateOf(false) }
    var expandedPostId by remember { mutableStateOf<Long?>(null) }
    var showCurrencyPicker by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Stage",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    // Currency selector
                    IconButton(onClick = { showCurrencyPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Monedă: ${selectedCurrency.displayName}"
                        )
                    }
                    
                    // Filter button
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Filtre"
                        )
                    }
                    
                    // Profile button
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPostClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adauga anunț"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    homeViewModel.setSearchQuery(it)
                },
                placeholder = { Text("Cauta anunțuri...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cauta"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.DEFAULT_PADDING.dp),
                singleLine = true
            )
            
            // Category filters
            if (showFilters) {
                CategoryFilterChips(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        homeViewModel.setCategory(category)
                    }
                )
            }
            
            // Content
            when (val currentState = postsState) {
                is com.example.stage.viewmodel.PostsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is com.example.stage.viewmodel.PostsState.Success -> {
                    if (currentState.posts.isEmpty()) {
                        EmptyState(
                            onAddPostClick = onAddPostClick
                        )
                    } else {
                        PostsList(
                            posts = currentState.posts,
                            selectedCurrency = selectedCurrency,
                            expandedPostId = expandedPostId,
                            onPostClick = { post ->
                                // Toggle expansion instead of navigation
                                expandedPostId = if (expandedPostId == post.id) null else post.id
                            }
                        )
                    }
                }
                is com.example.stage.viewmodel.PostsState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterChips(
    selectedCategory: PostCategory?,
    onCategorySelected: (PostCategory?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Constants.DEFAULT_PADDING.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Toate") }
            )
        }
        
        items(PostCategory.values()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) }
            )
        }
    }
}

@Composable
fun PostsList(
    posts: List<Post>,
    selectedCurrency: Currency,
    expandedPostId: Long?,
    onPostClick: (Post) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(Constants.DEFAULT_PADDING.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(
                post = post,
                selectedCurrency = selectedCurrency,
                isExpanded = expandedPostId == post.id,
                onClick = { onPostClick(post) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    selectedCurrency: Currency,
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isExpanded) 8.dp else Constants.DEFAULT_ELEVATION.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
        ) {
            // Header
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
                
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = post.category.displayName,
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
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Expanded details section
            if (isExpanded) {
                ExpandedPostDetails(post = post)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Text(
                    text = formatPrice(post.price, selectedCurrency),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Location and date
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (post.location != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Locație",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = post.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Text(
                        text = formatDate(post.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    onAddPostClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Constants.DEFAULT_PADDING.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Nu sunt anunțuri",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Nu sunt anunțuri disponibile",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Fii primul care adaugă un anunț!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onAddPostClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adaugă anunț")
        }
    }
}

private fun formatPrice(price: Double, currency: Currency): String {
    return when (currency) {
        Currency.RON -> "${price.toInt()} lei"
        Currency.EUR -> "€${price.toInt()}"
        Currency.USD -> "$${price.toInt()}"
        Currency.GBP -> "£${price.toInt()}"
        Currency.CHF -> "${price.toInt()} CHF"
        Currency.JPY -> "¥${price.toInt()}"
        Currency.CAD -> "C$${price.toInt()}"
        Currency.AUD -> "A$${price.toInt()}"
    }
}

private fun formatDate(timestamp: Long): String {
    // TODO: Implement proper date formatting
    return "Acum 1 ora"
}

@Composable
fun ExpandedPostDetails(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Detalii complete",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Contact information
            if (post.contactPhone != null || post.contactEmail != null) {
                Text(
                    text = "Contact",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (post.contactPhone != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Telefon",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = post.contactPhone,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                if (post.contactEmail != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = post.contactEmail,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Location
            if (post.location != null) {
                Text(
                    text = "Locație",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Locație",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.location,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            /*// Car details (if it's a car post)
            if (post.category == PostCategory.CAR) {
                Text(
                    text = "Detalii mașină",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // TODO: Add car details from CarDetails entity
                // For now, we'll show a placeholder
                Text(
                    text = "Detalii mașină vor fi afișate aici",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }*/
            
            // Post metadata
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Informații anunț",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "ID anunț",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ID: ${post.id}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Status",
                    modifier = Modifier.size(16.dp),
                    tint = if (post.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (post.isActive) "Activ" else "Inactiv",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (post.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CurrencyPickerDialog(
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Selectează moneda")
        },
        text = {
            Column {
                Currency.values().forEach { currency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCurrencySelected(currency) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currency == selectedCurrency,
                            onClick = { onCurrencySelected(currency) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currency.displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Închide")
            }
        }
    )
}

