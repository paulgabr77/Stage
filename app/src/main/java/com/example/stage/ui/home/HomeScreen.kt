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

/**
 * Ecranul principal (home) - lista de anunțuri.
 * Afișează toate anunțurile active cu posibilitatea de filtrare și căutare.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<Post> = emptyList(),
    selectedCurrency: Currency = Currency.RON,
    isLoading: Boolean = false,
    onPostClick: (Post) -> Unit = {},
    onAddPostClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCurrencyChange: (Currency) -> Unit = {},
    onCategoryFilter: (PostCategory?) -> Unit = {},
    onSearchQuery: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<PostCategory?>(null) }
    var showFilters by remember { mutableStateOf(false) }
    
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
                    IconButton(onClick = { /* TODO: Show currency picker */ }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Monedă: ${selectedCurrency.displayName}"
                        )
                    }
                    
                    // Filter button
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = Icons.Default.Star,
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
                    contentDescription = "Adaugă anunț"
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
                    searchQuery = it
                    onSearchQuery(it)
                },
                placeholder = { Text("Caută anunțuri...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Caută"
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
                        selectedCategory = category
                        onCategoryFilter(category)
                    }
                )
            }
            
            // Content
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                posts.isEmpty() -> {
                    EmptyState(
                        onAddPostClick = onAddPostClick
                    )
                }
                else -> {
                    PostsList(
                        posts = posts,
                        selectedCurrency = selectedCurrency,
                        onPostClick = onPostClick
                    )
                }
            }
        }
    }
}

/**
 * Chips pentru filtrarea după categorie.
 */
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

/**
 * Lista de anunțuri.
 */
@Composable
fun PostsList(
    posts: List<Post>,
    selectedCurrency: Currency,
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
                onClick = { onPostClick(post) }
            )
        }
    }
}

/**
 * Card pentru un anunț.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    selectedCurrency: Currency,
    onClick: () -> Unit
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
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

/**
 * Stare goală când nu sunt anunțuri.
 */
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

/**
 * Formatează prețul în moneda selectată.
 */
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

/**
 * Formatează data de creare.
 */
private fun formatDate(timestamp: Long): String {
    // TODO: Implement proper date formatting
    return "Acum 2 ore"
}

