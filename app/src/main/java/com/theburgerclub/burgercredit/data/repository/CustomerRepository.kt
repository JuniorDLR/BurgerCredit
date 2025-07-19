package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.CustomerDao
import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    suspend fun insertCustomer(customer: CustomerEntity): Long =
        customerDao.insertCustomer(customer)

    suspend fun updateCustomer(customer: CustomerEntity) =
        customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: CustomerEntity) =
        customerDao.deleteCustomer(customer)

    fun getAllCustomers(): Flow<List<CustomerEntity>> =
        customerDao.getAllCustomers()

    suspend fun getCustomerById(id: Long): CustomerEntity? =
        customerDao.getCustomerById(id)

    fun searchCustomersByName(name: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomersByName(name)
} 