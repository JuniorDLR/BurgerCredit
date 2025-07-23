package com.theburgerclub.burgercredit.domain.usecase

import com.theburgerclub.burgercredit.data.repository.CustomerRepository
import com.theburgerclub.burgercredit.domain.mapper.toDomain
import com.theburgerclub.burgercredit.domain.mapper.toEntity
import com.theburgerclub.burgercredit.domain.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.paging.PagingData
import androidx.paging.map
import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import javax.inject.Inject

class CustomerUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend fun addCustomer(customer: Customer): Long =
        repository.insertCustomer(customer.toEntity())

    suspend fun updateCustomer(customer: Customer) =
        repository.updateCustomer(customer.toEntity())

    suspend fun deleteCustomer(customer: Customer) =
        repository.deleteCustomer(customer.toEntity())

    fun getAllCustomers(): Flow<List<Customer>> =
        repository.getAllCustomers().map { list -> list.map { it.toDomain() } }



    suspend fun getCustomerByNameAndLastName(name: String, lastName: String): Customer? =
        repository.getCustomerByNameAndLastName(name, lastName)?.toDomain()

    fun getCustomersPaging(pageSize: Int = 10): Flow<PagingData<Customer>> =
        repository.getCustomersPaging(pageSize).map { pagingData: PagingData<CustomerEntity> ->
            pagingData.map { it.toDomain() }
        }

    fun searchCustomersPaging(name: String, pageSize: Int = 10): Flow<PagingData<Customer>> =
        repository.searchCustomersPaging(name, pageSize).map { pagingData: PagingData<CustomerEntity> ->
            pagingData.map { it.toDomain() }
        }
} 