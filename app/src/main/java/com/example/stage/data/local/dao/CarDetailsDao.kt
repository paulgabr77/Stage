package com.example.stage.data.local.dao

import androidx.room.*
import com.example.stage.data.local.entities.CarDetails
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) pentru entitatea CarDetails.
 * Definește toate operațiile pe care le putem face cu detaliile mașinilor în baza de date.
 */
@Dao
interface CarDetailsDao {
    
    /**
     * Inserează detalii noi pentru o mașină în baza de date.
     * @param carDetails detaliile de inserat
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarDetails(carDetails: CarDetails)
    
    /**
     * Actualizează detaliile existente pentru o mașină.
     * @param carDetails detaliile actualizate
     */
    @Update
    suspend fun updateCarDetails(carDetails: CarDetails)
    
    /**
     * Șterge detaliile unei mașini din baza de date.
     * @param carDetails detaliile de șters
     */
    @Delete
    suspend fun deleteCarDetails(carDetails: CarDetails)
    
    /**
     * Obține detaliile unei mașini după ID-ul anunțului.
     * @param postId ID-ul anunțului
     * @return detaliile mașinii găsite sau null
     */
    @Query("SELECT * FROM car_details WHERE postId = :postId")
    suspend fun getCarDetailsByPostId(postId: Long): CarDetails?
    
    /**
     * Obține toate detaliile mașinilor ca Flow.
     * @return Flow cu toate detaliile mașinilor
     */
    @Query("SELECT * FROM car_details")
    fun getAllCarDetails(): Flow<List<CarDetails>>
    
    /**
     * Caută mașini după marcă.
     * @param make marca mașinii (ex: BMW, Audi)
     * @return Flow cu detaliile mașinilor din marca specificată
     */
    @Query("SELECT * FROM car_details WHERE make LIKE '%' || :make || '%'")
    fun getCarDetailsByMake(make: String): Flow<List<CarDetails>>
    
    /**
     * Caută mașini după model.
     * @param model modelul mașinii (ex: X5, A4)
     * @return Flow cu detaliile mașinilor din modelul specificat
     */
    @Query("SELECT * FROM car_details WHERE model LIKE '%' || :model || '%'")
    fun getCarDetailsByModel(model: String): Flow<List<CarDetails>>
    
    /**
     * Obține mașinile într-un interval de ani.
     * @param minYear anul minim
     * @param maxYear anul maxim
     * @return Flow cu detaliile mașinilor din intervalul de ani
     */
    @Query("SELECT * FROM car_details WHERE year BETWEEN :minYear AND :maxYear ORDER BY year DESC")
    fun getCarDetailsByYearRange(minYear: Int, maxYear: Int): Flow<List<CarDetails>>
    
    /**
     * Obține mașinile după tipul de combustibil.
     * @param fuelType tipul de combustibil (ex: Diesel, Petrol, Electric)
     * @return Flow cu detaliile mașinilor cu tipul de combustibil specificat
     */
    @Query("SELECT * FROM car_details WHERE fuelType = :fuelType")
    fun getCarDetailsByFuelType(fuelType: String): Flow<List<CarDetails>>
    
    /**
     * Obține mașinile după tipul de transmisie.
     * @param transmission tipul de transmisie (ex: Manual, Automatic)
     * @return Flow cu detaliile mașinilor cu tipul de transmisie specificat
     */
    @Query("SELECT * FROM car_details WHERE transmission = :transmission")
    fun getCarDetailsByTransmission(transmission: String): Flow<List<CarDetails>>
    
    /**
     * Caută mașini după VIN (seria de caroserie).
     * @param vin seria de caroserie
     * @return detaliile mașinii cu VIN-ul specificat sau null
     */
    @Query("SELECT * FROM car_details WHERE vin = :vin")
    suspend fun getCarDetailsByVin(vin: String): CarDetails?
    
    /**
     * Verifică dacă există o mașină cu VIN-ul dat.
     * @param vin seria de caroserie
     * @return true dacă există, false altfel
     */
    @Query("SELECT EXISTS(SELECT 1 FROM car_details WHERE vin = :vin)")
    suspend fun carDetailsExistsByVin(vin: String): Boolean
    
    /**
     * Obține numărul total de mașini înregistrate.
     * @return numărul de mașini
     */
    @Query("SELECT COUNT(*) FROM car_details")
    suspend fun getCarDetailsCount(): Int
}

