package com.example.stage.ui.addpost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stage.data.local.entities.PostCategory
import com.example.stage.utils.Constants

/**
 * Ecranul pentru adăugarea unui anunț nou.
 * Permite utilizatorilor să creeze anunțuri pentru mașini sau piese.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    onPostCreated: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onShowError: (String) -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(PostCategory.CAR) }
    var location by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // Car details (only for CAR category)
    var vin by remember { mutableStateOf("") }
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var fuelType by remember { mutableStateOf("") }
    var transmission by remember { mutableStateOf("") }
    var engineSize by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adaugă anunț") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Înapoi"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val validationResult = validateForm(
                                title, description, price, category,
                                vin, make, model, year
                            )
                            if (validationResult.isValid) {
                                isLoading = true
                                // TODO: Call ViewModel createPost function
                                // viewModel.createPost(...)
                            } else {
                                onShowError(validationResult.errorMessage)
                            }
                        },
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Publică")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Basic information section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.DEFAULT_PADDING.dp),
                elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp)
            ) {
                Column(
                    modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
                ) {
                    Text(
                        text = "Informații de bază",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category selection
                    Text(
                        text = "Categorie",
                        style = MaterialTheme.typography.labelMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PostCategory.values().forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titlu anunț") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descriere") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Price
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Preț (RON)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Preț"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                }
            }
            
            // Car details section (only for CAR category)
            if (category == PostCategory.CAR) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Constants.DEFAULT_PADDING.dp),
                    elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
                    ) {
                        Text(
                            text = "Detalii mașină",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // VIN
                        OutlinedTextField(
                            value = vin,
                            onValueChange = { vin = it },
                            label = { Text("Seria de caroserie (VIN)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Make and Model
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = make,
                                onValueChange = { make = it },
                                label = { Text("Marcă") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            
                            OutlinedTextField(
                                value = model,
                                onValueChange = { model = it },
                                label = { Text("Model") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Year and Mileage
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = year,
                                onValueChange = { year = it },
                                label = { Text("An") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                            
                            OutlinedTextField(
                                value = mileage,
                                onValueChange = { mileage = it },
                                label = { Text("Kilometraj") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Fuel type and Transmission
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = fuelType,
                                onValueChange = { fuelType = it },
                                label = { Text("Combustibil") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            
                            OutlinedTextField(
                                value = transmission,
                                onValueChange = { transmission = it },
                                label = { Text("Transmisie") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Engine size and Color
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = engineSize,
                                onValueChange = { engineSize = it },
                                label = { Text("Motor") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                            
                            OutlinedTextField(
                                value = color,
                                onValueChange = { color = it },
                                label = { Text("Culoare") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Condition
                        OutlinedTextField(
                            value = condition,
                            onValueChange = { condition = it },
                            label = { Text("Stare") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    }
                }
            }
            
            // Contact information section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.DEFAULT_PADDING.dp),
                elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp)
            ) {
                Column(
                    modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
                ) {
                    Text(
                        text = "Informații contact",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Location
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Locație") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Locație"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contact phone
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text("Telefon contact") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Telefon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contact email
                    OutlinedTextField(
                        value = contactEmail,
                        onValueChange = { contactEmail = it },
                        label = { Text("Email contact") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Rezultatul validării formularului.
 */
data class AddPostValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)

/**
 * Validează formularul de adăugare anunț.
 */
private fun validateForm(
    title: String,
    description: String,
    price: String,
    category: PostCategory,
    vin: String,
    make: String,
    model: String,
    year: String
): AddPostValidationResult {
    if (title.isBlank()) {
        return AddPostValidationResult(false, "Titlul este obligatoriu")
    }
    
    if (title.length > Constants.MAX_TITLE_LENGTH) {
        return AddPostValidationResult(false, "Titlul este prea lung")
    }
    
    if (description.isBlank()) {
        return AddPostValidationResult(false, "Descrierea este obligatorie")
    }
    
    if (description.length > Constants.MAX_DESCRIPTION_LENGTH) {
        return AddPostValidationResult(false, "Descrierea este prea lungă")
    }
    
    val priceValue = price.toDoubleOrNull()
    if (priceValue == null || priceValue < Constants.MIN_PRICE || priceValue > Constants.MAX_PRICE) {
        return AddPostValidationResult(false, "Prețul trebuie să fie un număr valid între ${Constants.MIN_PRICE} și ${Constants.MAX_PRICE}")
    }
    
    // Additional validation for car category
    if (category == PostCategory.CAR) {
        if (make.isBlank()) {
            return AddPostValidationResult(false, "Marca este obligatorie pentru mașini")
        }
        
        if (model.isBlank()) {
            return AddPostValidationResult(false, "Modelul este obligatoriu pentru mașini")
        }
        
        val yearValue = year.toIntOrNull()
        if (yearValue == null || yearValue < 1900 || yearValue > 2024) {
            return AddPostValidationResult(false, "Anul trebuie să fie valid")
        }
    }
    
    return AddPostValidationResult(true)
}

