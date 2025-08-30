package com.example.stage.utils

/**
 * Constantele aplicației.
 * Conține toate valorile fixe folosite în aplicație.
 */
object Constants {
    
    // ==================== APP INFO ====================
    
    const val APP_NAME = "Stage"
    const val APP_VERSION = "1.0.0"
    const val APP_PACKAGE = "com.example.stage"
    
    // ==================== DATABASE ====================
    
    const val DATABASE_NAME = "stage_database"
    const val DATABASE_VERSION = 1
    
    // ==================== API ENDPOINTS ====================
    
    const val EXCHANGE_RATE_BASE_URL = "https://api.exchangerate-api.com/v4/"
    const val EXCHANGE_RATE_TIMEOUT = 30L // secunde
    
    // ==================== UI CONSTANTS ====================
    
    const val DEFAULT_PADDING = 16
    const val SMALL_PADDING = 8
    const val LARGE_PADDING = 24
    
    const val DEFAULT_CORNER_RADIUS = 8
    const val LARGE_CORNER_RADIUS = 16
    
    const val DEFAULT_ELEVATION = 4
    const val HIGH_ELEVATION = 8
    
    // ==================== ANIMATION DURATIONS ====================
    
    const val SHORT_ANIMATION_DURATION = 200L
    const val MEDIUM_ANIMATION_DURATION = 300L
    const val LONG_ANIMATION_DURATION = 500L
    
    // ==================== REFRESH INTERVALS ====================
    
    const val MIN_REFRESH_INTERVAL = 1 // minute
    const val MAX_REFRESH_INTERVAL = 60 // minute
    const val DEFAULT_REFRESH_INTERVAL = 5 // minute
    
    // ==================== VALIDATION RULES ====================
    
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_PASSWORD_LENGTH = 50
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 50
    const val MAX_TITLE_LENGTH = 100
    const val MAX_DESCRIPTION_LENGTH = 1000
    const val MAX_PRICE = 999999999.99
    const val MIN_PRICE = 0.01
    
    // ==================== IMAGE CONSTANTS ====================
    
    const val MAX_IMAGES_PER_POST = 10
    const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5MB
    const val IMAGE_QUALITY = 80 // procente
    
    // ==================== SEARCH CONSTANTS ====================
    
    const val MIN_SEARCH_QUERY_LENGTH = 2
    const val MAX_SEARCH_QUERY_LENGTH = 50
    const val SEARCH_DELAY = 500L // milisecunde
    
    // ==================== PAGINATION ====================
    
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 100
    
    // ==================== ERROR MESSAGES ====================
    
    object ErrorMessages {
        const val NETWORK_ERROR = "Eroare de conexiune. Verifică internetul."
        const val UNKNOWN_ERROR = "A apărut o eroare neașteptată."
        const val INVALID_EMAIL = "Adresa de email nu este validă."
        const val INVALID_PASSWORD = "Parola trebuie să aibă cel puțin 6 caractere."
        const val PASSWORDS_DONT_MATCH = "Parolele nu se potrivesc."
        const val EMAIL_ALREADY_EXISTS = "Această adresă de email este deja înregistrată."
        const val INVALID_CREDENTIALS = "Email sau parolă incorectă."
        const val REQUIRED_FIELD = "Acest câmp este obligatoriu."
        const val INVALID_PRICE = "Prețul trebuie să fie un număr pozitiv."
        const val INVALID_PHONE = "Numărul de telefon nu este valid."
        const val IMAGE_TOO_LARGE = "Imaginea este prea mare. Maxim 5MB."
        const val TOO_MANY_IMAGES = "Poți încărca maxim 10 imagini."
        const val VIN_INVALID = "Seria de caroserie nu este validă."
    }
    
    // ==================== SUCCESS MESSAGES ====================
    
    object SuccessMessages {
        const val LOGIN_SUCCESS = "Autentificare reușită!"
        const val REGISTER_SUCCESS = "Cont creat cu succes!"
        const val POST_CREATED = "Anunțul a fost creat cu succes!"
        const val POST_UPDATED = "Anunțul a fost actualizat cu succes!"
        const val POST_DELETED = "Anunțul a fost șters cu succes!"
        const val PROFILE_UPDATED = "Profilul a fost actualizat cu succes!"
        const val SETTINGS_SAVED = "Setările au fost salvate cu succes!"
    }
    
    // ==================== NAVIGATION ROUTES ====================
    
    object Routes {
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val HOME = "home"
        const val ADD_POST = "add_post"
        const val PROFILE = "profile"
        const val POST_DETAILS = "post_details"
        const val SETTINGS = "settings"
        const val SPLASH = "splash"
    }
    
    // ==================== SHARED PREFERENCES KEYS ====================
    
    object PrefKeys {
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val IS_LOGGED_IN = "is_logged_in"
        const val SELECTED_CURRENCY = "selected_currency"
        const val DARK_MODE = "dark_mode"
        const val LAST_VISITED_PAGE = "last_visited_page"
        const val FIRST_LAUNCH = "first_launch"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val AUTO_REFRESH_ENABLED = "auto_refresh_enabled"
        const val REFRESH_INTERVAL = "refresh_interval"
    }
    
    // ==================== DATE FORMATS ====================
    
    object DateFormats {
        const val DISPLAY_DATE = "dd.MM.yyyy"
        const val DISPLAY_DATE_TIME = "dd.MM.yyyy HH:mm"
        const val API_DATE = "yyyy-MM-dd"
        const val API_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
    }
    
    // ==================== CURRENCY FORMATS ====================
    
    object CurrencyFormats {
        const val RON_FORMAT = "#,##0.00 lei"
        const val EUR_FORMAT = "#,##0.00 €"
        const val USD_FORMAT = "$#,##0.00"
        const val GBP_FORMAT = "£#,##0.00"
    }
    
    // ==================== FILE EXTENSIONS ====================
    
    object FileExtensions {
        const val IMAGE_JPG = ".jpg"
        const val IMAGE_JPEG = ".jpeg"
        const val IMAGE_PNG = ".png"
        const val IMAGE_WEBP = ".webp"
    }
    
    // ==================== MIME TYPES ====================
    
    object MimeTypes {
        const val IMAGE_JPEG = "image/jpeg"
        const val IMAGE_PNG = "image/png"
        const val IMAGE_WEBP = "image/webp"
        const val ALL_IMAGES = "image/*"
    }
    
    // ==================== PERMISSIONS ====================
    
    object Permissions {
        const val CAMERA = "android.permission.CAMERA"
        const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
        const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
        const val INTERNET = "android.permission.INTERNET"
        const val ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE"
    }
    
    // ==================== REQUEST CODES ====================
    
    object RequestCodes {
        const val CAMERA_PERMISSION = 100
        const val STORAGE_PERMISSION = 101
        const val PICK_IMAGE = 102
        const val TAKE_PHOTO = 103
    }
}
