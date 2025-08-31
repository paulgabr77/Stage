package com.example.stage.data.local.dao

import androidx.room.*
import com.example.stage.data.local.entities.CarDetails
import kotlinx.coroutines.flow.Flow

//Definește toate operațiile pe care le putem face cu detaliile mașinilor în baza de date.

@Dao
interface CarDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarDetails(carDetails: CarDetails)

    @Update
    suspend fun updateCarDetails(carDetails: CarDetails)
    

    @Delete
    suspend fun deleteCarDetails(carDetails: CarDetails)

    @Query("SELECT * FROM car_details WHERE postId = :postId")
    suspend fun getCarDetailsByPostId(postId: Long): CarDetails?

    @Query("SELECT * FROM car_details")
    fun getAllCarDetails(): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE make LIKE '%' || :make || '%'")
    fun getCarDetailsByMake(make: String): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE model LIKE '%' || :model || '%'")
    fun getCarDetailsByModel(model: String): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE year BETWEEN :minYear AND :maxYear ORDER BY year DESC")
    fun getCarDetailsByYearRange(minYear: Int, maxYear: Int): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE fuelType = :fuelType")
    fun getCarDetailsByFuelType(fuelType: String): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE transmission = :transmission")
    fun getCarDetailsByTransmission(transmission: String): Flow<List<CarDetails>>

    @Query("SELECT * FROM car_details WHERE vin = :vin")
    suspend fun getCarDetailsByVin(vin: String): CarDetails?

    @Query("SELECT EXISTS(SELECT 1 FROM car_details WHERE vin = :vin)")
    suspend fun carDetailsExistsByVin(vin: String): Boolean

    @Query("SELECT COUNT(*) FROM car_details")
    suspend fun getCarDetailsCount(): Int
}

