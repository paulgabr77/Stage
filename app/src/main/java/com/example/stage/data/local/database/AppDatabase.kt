package com.example.stage.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.stage.data.local.converters.Converters
import com.example.stage.data.local.dao.UserDao
import com.example.stage.data.local.dao.PostDao
import com.example.stage.data.local.dao.CarDetailsDao
import com.example.stage.data.local.entities.User
import com.example.stage.data.local.entities.Post
import com.example.stage.data.local.entities.CarDetails

// Baza de date principala a aplicatiei.
@Database(
    entities = [
        User::class,
        Post::class,
        CarDetails::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    //DAO pentru operațiile cu utilizatori.
    abstract fun userDao(): UserDao
    
    //DAO pentru operațiile cu anunțuri.
    abstract fun postDao(): PostDao
    
    //DAO pentru operațiile cu detaliile mașinilor.
    abstract fun carDetailsDao(): CarDetailsDao
    
    companion object {
        private const val DATABASE_NAME = "stage_database"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration() // Șterge baza de date la migrari (pentru dezvoltare)
                .build()
                INSTANCE = instance
                instance
            }
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
