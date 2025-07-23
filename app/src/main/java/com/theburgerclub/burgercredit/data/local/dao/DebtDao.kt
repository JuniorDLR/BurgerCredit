package com.theburgerclub.burgercredit.data.local.dao

import androidx.room.*
import com.theburgerclub.burgercredit.data.local.entity.DebtEntity
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource

@Dao
interface DebtDao {
    // Insert a debt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity): Long

    // Update a debt
    @Update
    suspend fun updateDebt(debt: DebtEntity)

    // Delete a debt
    @Delete
    suspend fun deleteDebt(debt: DebtEntity)

    // Get all debts
    @Query("SELECT * FROM debts ORDER BY dueDate ASC")
    fun getAllDebts(): Flow<List<DebtEntity>>

    // Get a debt by ID
    @Query("SELECT * FROM debts WHERE id = :id LIMIT 1")
    suspend fun getDebtById(id: Long): DebtEntity?


    // Get active debts
    @Query("SELECT * FROM debts WHERE isActive = 1 ORDER BY dueDate ASC")
    fun getActiveDebts(): Flow<List<DebtEntity>>

    // Count customers with active debt
    @Query("SELECT COUNT(DISTINCT customerId) FROM debts WHERE isActive = 1")
    fun getTotalCustomersWithActiveDebt(): Flow<Int>

    // Sum total pending amount
    @Query("SELECT SUM(amount) FROM debts WHERE isActive = 1")
    fun getTotalPendingAmount(): Flow<Double?>

    // Count number of active debts
    @Query("SELECT COUNT(*) FROM debts WHERE isActive = 1")
    fun getNumberOfActiveDebts(): Flow<Int>


    @Query("UPDATE debts SET isActive = :isActive WHERE id = :debtId")
    suspend fun updateDebtActiveStatus(debtId: Long, isActive: Boolean)

    @Query("SELECT * FROM debts ORDER BY dueDate ASC")
    fun pagingSource(): PagingSource<Int, DebtEntity>

    @Query("SELECT * FROM debts WHERE isActive = 1 ORDER BY dueDate ASC")
    fun pagingSourceActive(): PagingSource<Int, DebtEntity>
} 