package com.example.stage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitatea CarDetails conține informații specifice pentru mașini.
 * Aceasta entitate este legata de Post prin postId
 */
@Entity(tableName = "car_details")
data class CarDetails(
    @PrimaryKey
    val postId: Long,  // ID-ul anunțului (Post) căruia îi aparține
    val vin: String? = null,  // Seria de caroserie (Vehicle Identification Number)
    val make: String? = null,  // Marca (ex: BMW, Audi)
    val model: String? = null,  // Modelul (ex: X5, A4)
    val year: Int? = null,  // Anul de fabricație
    val mileage: Int? = null,  // Kilometrajul
    val fuelType: String? = null,  // Tipul de combustibil
    val transmission: String? = null,  // Tipul de transmisie
    val engineSize: String? = null,  // Capacitatea motorului
    val color: String? = null,  // Culoarea
    val condition: String? = null  // Starea masinii
)
