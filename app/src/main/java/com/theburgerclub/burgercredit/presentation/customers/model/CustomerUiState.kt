package com.theburgerclub.burgercredit.presentation.customers.model

import com.theburgerclub.burgercredit.domain.model.Customer

data class CustomerUiState(
    val customers: List<Customer> = emptyList(),
    val selectedCustomer: Customer? = null,
    val customerInput: String = "",
    val lastNameInput: String = "",
    val customerInputError: String? = null,
    val lastNameInputError: String? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<Customer> = emptyList(),
    val isEdit: Boolean = false,
    val customersDebtsCount: Map<Customer, Int> = emptyMap()
) 