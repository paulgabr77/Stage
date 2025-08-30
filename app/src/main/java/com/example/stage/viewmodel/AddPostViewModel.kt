package com.example.stage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.CarDetails
import com.example.stage.data.local.entities.PostCategory
import com.example.stage.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel pentru ecranul de adăugare anunț.
 * Gestionează crearea și validarea anunțurilor noi.
 */
class AddPostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    
    // State pentru crearea anunțului
    private val _addPostState = MutableStateFlow<AddPostState>(AddPostState.Idle)
    val addPostState: StateFlow<AddPostState> = _addPostState.asStateFlow()
    
    // Datele anunțului
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()
    
    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price.asStateFlow()
    
    private val _category = MutableStateFlow(PostCategory.CAR)
    val category: StateFlow<PostCategory> = _category.asStateFlow()
    
    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images.asStateFlow()
    
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()
    
    private val _contactPhone = MutableStateFlow("")
    val contactPhone: StateFlow<String> = _contactPhone.asStateFlow()
    
    private val _contactEmail = MutableStateFlow("")
    val contactEmail: StateFlow<String> = _contactEmail.asStateFlow()
    
    // Detaliile mașinii (doar pentru anunțuri de tip CAR)
    private val _vin = MutableStateFlow("")
    val vin: StateFlow<String> = _vin.asStateFlow()
    
    private val _make = MutableStateFlow("")
    val make: StateFlow<String> = _make.asStateFlow()
    
    private val _model = MutableStateFlow("")
    val model: StateFlow<String> = _model.asStateFlow()
    
    private val _year = MutableStateFlow("")
    val year: StateFlow<String> = _year.asStateFlow()
    
    private val _mileage = MutableStateFlow("")
    val mileage: StateFlow<String> = _mileage.asStateFlow()
    
    private val _fuelType = MutableStateFlow("")
    val fuelType: StateFlow<String> = _fuelType.asStateFlow()
    
    private val _transmission = MutableStateFlow("")
    val transmission: StateFlow<String> = _transmission.asStateFlow()
    
    private val _engineSize = MutableStateFlow("")
    val engineSize: StateFlow<String> = _engineSize.asStateFlow()
    
    private val _color = MutableStateFlow("")
    val color: StateFlow<String> = _color.asStateFlow()
    
    private val _condition = MutableStateFlow("")
    val condition: StateFlow<String> = _condition.asStateFlow()
    
    /**
     * Setează titlul anunțului.
     */
    fun setTitle(title: String) {
        _title.value = title
    }
    
    /**
     * Setează descrierea anunțului.
     */
    fun setDescription(description: String) {
        _description.value = description
    }
    
    /**
     * Setează prețul anunțului.
     */
    fun setPrice(price: String) {
        _price.value = price
    }
    
    /**
     * Setează categoria anunțului.
     */
    fun setCategory(category: PostCategory) {
        _category.value = category
    }
    
    /**
     * Setează imaginile anunțului.
     */
    fun setImages(images: List<String>) {
        _images.value = images
    }
    
    /**
     * Setează locația anunțului.
     */
    fun setLocation(location: String) {
        _location.value = location
    }
    
    /**
     * Setează telefonul de contact.
     */
    fun setContactPhone(phone: String) {
        _contactPhone.value = phone
    }
    
    /**
     * Setează emailul de contact.
     */
    fun setContactEmail(email: String) {
        _contactEmail.value = email
    }
    
    /**
     * Setează VIN-ul mașinii.
     */
    fun setVin(vin: String) {
        _vin.value = vin
    }
    
    /**
     * Setează marca mașinii.
     */
    fun setMake(make: String) {
        _make.value = make
    }
    
    /**
     * Setează modelul mașinii.
     */
    fun setModel(model: String) {
        _model.value = model
    }
    
    /**
     * Setează anul mașinii.
     */
    fun setYear(year: String) {
        _year.value = year
    }
    
    /**
     * Setează kilometrajul mașinii.
     */
    fun setMileage(mileage: String) {
        _mileage.value = mileage
    }
    
    /**
     * Setează tipul de combustibil.
     */
    fun setFuelType(fuelType: String) {
        _fuelType.value = fuelType
    }
    
    /**
     * Setează tipul de transmisie.
     */
    fun setTransmission(transmission: String) {
        _transmission.value = transmission
    }
    
    /**
     * Setează capacitatea motorului.
     */
    fun setEngineSize(engineSize: String) {
        _engineSize.value = engineSize
    }
    
    /**
     * Setează culoarea mașinii.
     */
    fun setColor(color: String) {
        _color.value = color
    }
    
    /**
     * Setează starea mașinii.
     */
    fun setCondition(condition: String) {
        _condition.value = condition
    }
    
    /**
     * Creează anunțul nou.
     * 
     * @param userId ID-ul utilizatorului care creează anunțul
     */
    fun createPost(userId: Long) {
        if (!validateForm()) {
            return
        }
        
        _addPostState.value = AddPostState.Loading
        
        viewModelScope.launch {
            try {
                val post = Post(
                    userId = userId,
                    title = _title.value.trim(),
                    description = _description.value.trim(),
                    price = _price.value.toDoubleOrNull() ?: 0.0,
                    category = _category.value,
                    images = _images.value,
                    location = _location.value.trim().takeIf { it.isNotBlank() },
                    contactPhone = _contactPhone.value.trim().takeIf { it.isNotBlank() },
                    contactEmail = _contactEmail.value.trim().takeIf { it.isNotBlank() }
                )
                
                val carDetails = if (_category.value == PostCategory.CAR) {
                    CarDetails(
                        postId = 0, // Va fi setat de repository
                        vin = _vin.value.trim().takeIf { it.isNotBlank() },
                        make = _make.value.trim().takeIf { it.isNotBlank() },
                        model = _model.value.trim().takeIf { it.isNotBlank() },
                        year = _year.value.toIntOrNull(),
                        mileage = _mileage.value.toIntOrNull(),
                        fuelType = _fuelType.value.trim().takeIf { it.isNotBlank() },
                        transmission = _transmission.value.trim().takeIf { it.isNotBlank() },
                        engineSize = _engineSize.value.trim().takeIf { it.isNotBlank() },
                        color = _color.value.trim().takeIf { it.isNotBlank() },
                        condition = _condition.value.trim().takeIf { it.isNotBlank() }
                    )
                } else null
                
                val postId = postRepository.createPost(post, carDetails)
                _addPostState.value = AddPostState.Success(postId)
                
            } catch (e: Exception) {
                _addPostState.value = AddPostState.Error("Eroare la crearea anunțului: ${e.message}")
            }
        }
    }
    
    /**
     * Validează formularul de creare anunț.
     * 
     * @return true dacă formularul este valid, false altfel
     */
    private fun validateForm(): Boolean {
        if (_title.value.trim().isBlank()) {
            _addPostState.value = AddPostState.Error("Titlul este obligatoriu")
            return false
        }
        
        if (_description.value.trim().isBlank()) {
            _addPostState.value = AddPostState.Error("Descrierea este obligatorie")
            return false
        }
        
        val price = _price.value.toDoubleOrNull()
        if (price == null || price <= 0) {
            _addPostState.value = AddPostState.Error("Prețul trebuie să fie un număr pozitiv")
            return false
        }
        
        return true
    }
    
    /**
     * Resetează starea de creare anunț.
     */
    fun resetState() {
        _addPostState.value = AddPostState.Idle
    }
    
    /**
     * Resetează toate câmpurile formularului.
     */
    fun resetForm() {
        _title.value = ""
        _description.value = ""
        _price.value = ""
        _category.value = PostCategory.CAR
        _images.value = emptyList()
        _location.value = ""
        _contactPhone.value = ""
        _contactEmail.value = ""
        _vin.value = ""
        _make.value = ""
        _model.value = ""
        _year.value = ""
        _mileage.value = ""
        _fuelType.value = ""
        _transmission.value = ""
        _engineSize.value = ""
        _color.value = ""
        _condition.value = ""
    }
}

/**
 * Stările pentru crearea anunțului.
 */
sealed class AddPostState {
    object Idle : AddPostState()
    object Loading : AddPostState()
    data class Success(val postId: Long) : AddPostState()
    data class Error(val message: String) : AddPostState()
}
