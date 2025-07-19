package com.theburgerclub.burgercredit.domain.usecase

import com.theburgerclub.burgercredit.data.repository.DebtRepository
import com.theburgerclub.burgercredit.domain.mapper.toDomain
import com.theburgerclub.burgercredit.domain.mapper.toEntity
import com.theburgerclub.burgercredit.domain.model.Debt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DebtUseCase @Inject constructor(
    private val repository: DebtRepository
) {
    suspend fun addDebt(debt: Debt): Long =
        repository.insertDebt(debt.toEntity())

    suspend fun updateDebt(debt: Debt) =
        repository.updateDebt(debt.toEntity())

    suspend fun deleteDebt(debt: Debt) =
        repository.deleteDebt(debt.toEntity())

    fun getAllDebts(): Flow<List<Debt>> =
        repository.getAllDebts().map { list -> list.map { it.toDomain() } }

    suspend fun getDebtById(id: Long): Debt? =
        repository.getDebtById(id)?.toDomain()

    fun getDebtsByCustomer(customerId: Long): Flow<List<Debt>> =
        repository.getDebtsByCustomer(customerId).map { list -> list.map { it.toDomain() } }

    fun getActiveDebts(): Flow<List<Debt>> =
        repository.getActiveDebts().map { list -> list.map { it.toDomain() } }

    fun getTotalCustomersWithActiveDebt(): Flow<Int> =
        repository.getTotalCustomersWithActiveDebt()

    fun getTotalPendingAmount(): Flow<Double?> =
        repository.getTotalPendingAmount()

    fun getNumberOfActiveDebts(): Flow<Int> =
        repository.getNumberOfActiveDebts()

    fun searchDebtsByDescription(desc: String): Flow<List<Debt>> =
        repository.searchDebtsByDescription(desc).map { list -> list.map { it.toDomain() } }
} 