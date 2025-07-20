package com.theburgerclub.burgercredit.presentation.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.usecase.CustomerUseCase
import com.theburgerclub.burgercredit.presentation.customers.model.CustomerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun onCustomerInputChange(newValue: String) {
        _customerUiState.update { it.copy(customerInput = newValue, customerInputError = null) }
    }

    fun onLastNameInputChange(newValue: String) {
        _customerUiState.update { it.copy(lastNameInput = newValue, lastNameInputError = null) }
    }

    fun clearCustomerInput() {
        _customerUiState.update { 
            it.copy(
                customerInput = "",
                lastNameInput = "",
                customerInputError = null,
                lastNameInputError = null,
                isLoading = false
            )
        }
    }

    fun clearErrors() {
        _customerUiState.update { 
            it.copy(
                customerInputError = null,
                lastNameInputError = null,
                isLoading = false
            )
        }
    }

    suspend fun validateAndAddCustomer(): Boolean {
        val name = customerUiState.value.customerInput
        val lastName = customerUiState.value.lastNameInput
        
        var hasErrors = false
        
        // Validar nombre
        if (name.isBlank()) {
            _customerUiState.update { it.copy(customerInputError = "Name cannot be empty") }
            hasErrors = true
        } else {
            _customerUiState.update { it.copy(customerInputError = null) }
        }
        
        // Validar apellido
        if (lastName.isBlank()) {
            _customerUiState.update { it.copy(lastNameInputError = "Last name cannot be empty") }
            hasErrors = true
        } else {
            _customerUiState.update { it.copy(lastNameInputError = null) }
        }
        
        // Si hay errores, no continuar
        if (hasErrors) {
            return false
        }
        
        // Activar loading
        _customerUiState.update { it.copy(isLoading = true) }
        
        try {
            // Pequeño delay para que se vea el loading
            delay(1000)
            
            // Verificar si ya existe un cliente con el mismo nombre y apellido (case-insensitive)
            val existingCustomer = customerUseCase.getCustomerByNameAndLastName(name.lowercase(), lastName.lowercase())
            if (existingCustomer != null) {
                _customerUiState.update { 
                    it.copy(
                        isLoading = false,
                        customerInputError = "A customer with this name and last name already exists"
                    )
                }
                return false
            }
            
            // Si no existe, agregar el cliente
            customerUseCase.addCustomer(Customer(name = name, lastName = lastName))
            clearCustomerInput()
            _customerUiState.update { it.copy(isLoading = false) }
            return true
        } catch (e: Exception) {
            _customerUiState.update { 
                it.copy(
                    isLoading = false,
                    customerInputError = e.message ?: "Unknown error"
                )
            }
            return false
        }
    }
} 
