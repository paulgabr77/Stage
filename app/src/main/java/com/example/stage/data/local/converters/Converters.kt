package com.example.stage.data.local.converters

import androidx.room.TypeConverter
import com.example.stage.data.local.entities.PostCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return if (value == null) null else {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun fromPostCategory(category: PostCategory?): String? {
        return category?.name
    }
    @TypeConverter
    fun toPostCategory(category: String?): PostCategory? {
        return if (category == null) null else PostCategory.valueOf(category)
    }
}

