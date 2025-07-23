package com.theburgerclub.burgercredit.data.repository

import com.theburgerclub.burgercredit.data.local.dao.CustomerDao
import com.theburgerclub.burgercredit.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    suspend fun getCustomerByNameAndLastName(name: String, lastName: String): CustomerEntity? =
        customerDao.getCustomerByNameAndLastName(name, lastName)

    fun getCustomersPaging(pageSize: Int = 10): Flow<PagingData<CustomerEntity>> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { customerDao.pagingSource() }
        ).flow

    fun searchCustomersPaging(name: String, pageSize: Int = 10): Flow<PagingData<CustomerEntity>> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { customerDao.pagingSourceByName(name) }
        ).flow
} 