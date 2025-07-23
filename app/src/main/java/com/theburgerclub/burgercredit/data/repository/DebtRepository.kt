package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.DebtDao
import com.theburgerclub.burgercredit.data.local.entity.DebtEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepository @Inject constructor(
    private val debtDao: DebtDao
) {
    suspend fun insertDebt(debt: DebtEntity): Long =
        debtDao.insertDebt(debt)

    suspend fun updateDebt(debt: DebtEntity) =
        debtDao.updateDebt(debt)

    suspend fun deleteDebt(debt: DebtEntity) =
        debtDao.deleteDebt(debt)

    fun getAllDebts(): Flow<List<DebtEntity>> =
        debtDao.getAllDebts()

    suspend fun getDebtById(id: Long): DebtEntity? =
        debtDao.getDebtById(id)

    fun getDebtsByCustomer(customerId: Long): Flow<List<DebtEntity>> =
        debtDao.getDebtsByCustomer(customerId)

    fun getActiveDebts(): Flow<List<DebtEntity>> =
        debtDao.getActiveDebts()

    fun getTotalCustomersWithActiveDebt(): Flow<Int> =
        debtDao.getTotalCustomersWithActiveDebt()

    fun getTotalPendingAmount(): Flow<Double?> =
        debtDao.getTotalPendingAmount()

    fun getNumberOfActiveDebts(): Flow<Int> =
        debtDao.getNumberOfActiveDebts()

    fun searchDebtsByDescription(desc: String): Flow<List<DebtEntity>> =
        debtDao.searchDebtsByDescription(desc)

    suspend fun updateDebtActiveStatus(debtId: Long, isActive: Boolean) =
        debtDao.updateDebtActiveStatus(debtId, isActive)
} 