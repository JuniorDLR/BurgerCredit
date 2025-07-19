package com.theburgerclub.burgercredit.presentation.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.usecase.CustomerUseCase
import com.theburgerclub.burgercredit.presentation.customers.model.CustomerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase
) : ViewModel() {

    private val _customerUiState = MutableStateFlow(CustomerUiState())
    val customerUiState: StateFlow<CustomerUiState> = _customerUiState.asStateFlow()

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            customerUseCase.getAllCustomers().collect { list ->
                _customerUiState.update { it.copy(customers = list) }
            }
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.addCustomer(customer)
            loadCustomers()
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.updateCustomer(customer)
            loadCustomers()
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.deleteCustomer(customer)
            loadCustomers()
        }
    }

    fun selectCustomer(customer: Customer?) {
        _customerUiState.update { it.copy(selectedCustomer = customer) }
    }

    fun searchCustomersByName(name: String) {
        viewModelScope.launch {
            customerUseCase.searchCustomersByName(name).collect { list ->
                _customerUiState.update { it.copy(customers = list) }
            }
        }
    }
} 