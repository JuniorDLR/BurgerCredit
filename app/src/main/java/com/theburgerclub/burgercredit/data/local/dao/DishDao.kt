package com.theburgerclub.burgercredit.data.local.dao

import androidx.room.*
import com.theburgerclub.burgercredit.data.local.entity.DishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    // Insertar un plato
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: DishEntity): Long

    // Actualizar un plato
    @Update
    suspend fun updateDish(dish: DishEntity)

    // Eliminar un plato
    @Delete
    suspend fun deleteDish(dish: DishEntity)

    // Obtener todos los platos
    @Query("SELECT * FROM dishes ORDER BY name ASC")
    fun getAllDishes(): Flow<List<DishEntity>>

    // Obtener un plato por ID
    @Query("SELECT * FROM dishes WHERE id = :id LIMIT 1")
    suspend fun getDishById(id: Long): DishEntity?

    // Buscar platos por nombre
    @Query("SELECT * FROM dishes WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    fun searchDishesByName(name: String): Flow<List<DishEntity>>

    // Obtener un plato por nombre exacto (case-insensitive)
    @Query("SELECT * FROM dishes WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name)) LIMIT 1")
    suspend fun getDishByName(name: String): DishEntity?
} 