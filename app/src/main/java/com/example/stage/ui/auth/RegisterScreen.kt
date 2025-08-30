package com.example.stage.ui.auth

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stage.utils.Constants
import com.example.stage.viewmodel.AuthViewModel
import com.example.stage.viewmodel.RegisterState

/**
 * Ecranul de înregistrare (register).
 * Permite utilizatorilor să își creeze un cont nou.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowError: (String) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    println("DEBUG: RegisterScreen composable called, viewModel=$viewModel")
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    val registerState by viewModel.registerState.collectAsState()
    val isLoading = registerState is RegisterState.Loading
    
    println("DEBUG: RegisterScreen recomposed, registerState=$registerState, isLoading=$isLoading")
    
    // Handle register state changes
    LaunchedEffect(registerState) {
        val currentState = registerState
        println("DEBUG: LaunchedEffect triggered with state: $currentState")
        when (currentState) {
            is RegisterState.Success -> {
                println("DEBUG: Register success, calling onRegisterSuccess")
                onRegisterSuccess()
            }
            is RegisterState.Error -> {
                println("DEBUG: Register error: ${currentState.message}")
                onShowError(currentState.message)
            }
            else -> {
                println("DEBUG: Register state: $currentState")
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Constants.DEFAULT_PADDING.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Creează cont",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = "Completează datele pentru a crea contul tău",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Formular de înregistrare
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(Constants.DEFAULT_ELEVATION.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(Constants.DEFAULT_PADDING.dp)
            ) {
                // Nume field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nume complet") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Nume"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Phone field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefon (opțional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Telefon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Parolă") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Parolă"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = if (passwordVisible) "Ascunde parola" else "Arată parola"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Confirm password field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmă parola") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Confirmă parola"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = if (confirmPasswordVisible) "Ascunde parola" else "Arată parola"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Register button
                Button(
                    onClick = {
                        println("DEBUG: Butonul de register a fost apăsat")
                        println("DEBUG: name=$name, email=$email, password=${password.length} chars, confirmPassword=${confirmPassword.length} chars")
                        
                        // Test simplu - să vedem dacă butonul funcționează
                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                            println("DEBUG: Validare simplă OK, apel viewModel.register")
                            viewModel.register(name, email, password, phone)
                        } else {
                            println("DEBUG: Validare simplă eșuată")
                            onShowError("Te rog completează toate câmpurile și confirmă parola")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Creează cont")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Login link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ai deja cont? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Autentifică-te")
                    }
                }
            }
        }
    }
}

/**
 * Rezultatul validării formularului.
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)

/**
 * Validează formularul de înregistrare.
 */
private fun validateForm(
    name: String,
    email: String,
    password: String,
    confirmPassword: String
): ValidationResult {
    if (name.isBlank()) {
        return ValidationResult(false, "Numele este obligatoriu")
    }
    
    if (name.length < Constants.MIN_NAME_LENGTH) {
        return ValidationResult(false, "Numele trebuie să aibă cel puțin ${Constants.MIN_NAME_LENGTH} caractere")
    }
    
    if (email.isBlank()) {
        return ValidationResult(false, "Email-ul este obligatoriu")
    }
    
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return ValidationResult(false, "Email-ul nu este valid")
    }
    
    if (password.isBlank()) {
        return ValidationResult(false, "Parola este obligatorie")
    }
    
    if (password.length < Constants.MIN_PASSWORD_LENGTH) {
        return ValidationResult(false, "Parola trebuie să aibă cel puțin ${Constants.MIN_PASSWORD_LENGTH} caractere")
    }
    
    if (confirmPassword.isBlank()) {
        return ValidationResult(false, "Confirmarea parolei este obligatorie")
    }
    
    if (password != confirmPassword) {
        return ValidationResult(false, "Parolele nu se potrivesc")
    }
    
    return ValidationResult(true)
}

