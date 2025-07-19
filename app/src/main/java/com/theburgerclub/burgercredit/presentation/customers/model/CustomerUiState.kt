package com.theburgerclub.burgercredit.presentation.customers.model

import com.theburgerclub.burgercredit.domain.model.Customer

data class CustomerUiState(
    val customers: List<Customer> = emptyList(),
    val selectedCustomer: Customer? = null
) 