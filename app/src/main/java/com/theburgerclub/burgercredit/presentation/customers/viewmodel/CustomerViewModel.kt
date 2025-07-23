package com.theburgerclub.burgercredit.presentation.customers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.usecase.CustomerUseCase
import com.theburgerclub.burgercredit.domain.usecase.DebtUseCase
import com.theburgerclub.burgercredit.presentation.customers.model.CustomerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.debounce

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase,
    private val debtUseCase: DebtUseCase
) : ViewModel() {

    private val _customerUiState = MutableStateFlow(CustomerUiState())
    val customerUiState: StateFlow<CustomerUiState> = _customerUiState.asStateFlow()


    // Paging
    private val _pagingSearchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    private val debouncedPagingQuery = _pagingSearchQuery.debounce(400)

    @OptIn(ExperimentalCoroutinesApi::class)
    val customersPaging = debouncedPagingQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                customerUseCase.getCustomersPaging()
            } else {
                customerUseCase.searchCustomersPaging(query)
            }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())


    init {
        observeCustomersAndDebts()
    }


    private fun observeCustomersAndDebts() {
        viewModelScope.launch {
            combine(
                customerUseCase.getAllCustomers(),
                debtUseCase.getAllDebts()
            ) { customers, debts ->
                val customersWithDebts = customers.map { customer ->
                    val activeDebts = debts.filter { it.customerId == customer.id && it.isActive }
                    customer to activeDebts.size
                }
                customers to customersWithDebts.toMap()
            }.collect { (customers, customersDebtsCount) ->
                _customerUiState.update {
                    it.copy(
                        customers = customers,
                        customersDebtsCount = customersDebtsCount,
                    )
                }
            }
        }
    }

    private fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.addCustomer(customer)

        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.updateCustomer(customer)

        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCase.deleteCustomer(customer)

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

    fun updateSearchQuery(query: String) {
        _customerUiState.update { it: CustomerUiState ->
            it.copy(
                searchQuery = query
            )
        }
        _pagingSearchQuery.value = query
    }


    fun startEditCustomer(customer: Customer) {
        _customerUiState.update {
            it.copy(
                isEdit = true,
                selectedCustomer = customer,
                customerInput = customer.name,
                lastNameInput = customer.lastName,
                customerInputError = null,
                lastNameInputError = null
            )
        }
    }

    fun exitEditMode() {
        _customerUiState.update {
            it.copy(
                isEdit = false,
                selectedCustomer = null,
                customerInput = "",
                lastNameInput = "",
                customerInputError = null,
                lastNameInputError = null
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
            val existingCustomer = customerUseCase.getCustomerByNameAndLastName(
                name.lowercase().trim(),
                lastName.lowercase().trim()
            )
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
            addCustomer(Customer(name = name, lastName = lastName))
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

    suspend fun validateAndUpdateCustomer(): Boolean {
        val name = customerUiState.value.customerInput
        val lastName = customerUiState.value.lastNameInput
        val selected = customerUiState.value.selectedCustomer

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

        // Si no hay cambios, mostrar error
        if (selected != null && name.trim() == selected.name.trim() && lastName.trim() == selected.lastName.trim()) {
            _customerUiState.update {
                it.copy(
                    customerInputError = "No changes detected. Please modify the name or last name.",
                    isLoading = false
                )
            }
            return false
        }

        // Activar loading
        _customerUiState.update { it.copy(isLoading = true) }

        try {
            // Pequeño delay para que se vea el loading
            delay(1000)

            // Verificar si ya existe un cliente con el mismo nombre y apellido (case-insensitive), excepto el propio
            val existingCustomer = customerUseCase.getCustomerByNameAndLastName(
                name.lowercase().trim(),
                lastName.lowercase().trim()
            )
            if (existingCustomer != null && existingCustomer.id != selected?.id) {
                _customerUiState.update {
                    it.copy(
                        isLoading = false,
                        customerInputError = "A customer with this name and last name already exists"
                    )
                }
                return false
            }

            // Si no existe duplicado, actualizar el cliente
            if (selected != null) {
                updateCustomer(selected.copy(name = name, lastName = lastName))
            }
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
