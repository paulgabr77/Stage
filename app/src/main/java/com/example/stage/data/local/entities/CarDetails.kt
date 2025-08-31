package com.example.stage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entitatea CarDetails conține informații specifice pentru mașini.
@Entity(tableName = "car_details")
data class CarDetails(
    @PrimaryKey
    val postId: Long,
    val vin: String? = null,
    val make: String? = null,
    val model: String? = null,
    val year: Int? = null,
    val mileage: Int? = null,
    val fuelType: String? = null,
    val transmission: String? = null,
    val engineSize: String? = null,
    val color: String? = null,
    val condition: String? = null
)
