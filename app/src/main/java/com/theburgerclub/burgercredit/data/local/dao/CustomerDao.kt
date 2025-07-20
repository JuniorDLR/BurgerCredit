package com.theburgerclub.burgercredit.data.local.dao

import androidx.room.*
import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    // Insertar un cliente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    // Actualizar un cliente
    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    // Eliminar un cliente
    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    // Obtener todos los clientes
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    // Obtener un cliente por ID
    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    suspend fun getCustomerById(id: Long): CustomerEntity?

    // Buscar clientes por nombre
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    fun searchCustomersByName(name: String): Flow<List<CustomerEntity>>

    // Verificar si existe un cliente con el mismo nombre y apellido
    @Query("SELECT * FROM customers WHERE LOWER(name) = LOWER(:name) AND LOWER(lastName) = LOWER(:lastName) LIMIT 1")
    suspend fun getCustomerByNameAndLastName(name: String, lastName: String): CustomerEntity?
} 