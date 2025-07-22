package com.theburgerclub.burgercredit.presentation.debt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.domain.usecase.CustomerUseCase
import com.theburgerclub.burgercredit.domain.usecase.DebtUseCase
import com.theburgerclub.burgercredit.domain.usecase.DishUseCase
import com.theburgerclub.burgercredit.presentation.debt.model.CustomerDebtGroup
import com.theburgerclub.burgercredit.presentation.debt.model.DebtUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DebtViewModel @Inject constructor(
    private val debtUseCase: DebtUseCase,
    private val customerUseCase: CustomerUseCase,
    private val dishUseCase: DishUseCase
) : ViewModel() {

    private val _debtUiState = MutableStateFlow(DebtUiState())
    val debtUiState: StateFlow<DebtUiState> = _debtUiState.asStateFlow()

    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    private val _customerSearchQuery = MutableStateFlow("")
    val filteredCustomers: StateFlow<List<Customer>> = _customerSearchQuery
        .combine(_customers) { query, customerList ->
            if (query.isBlank()) emptyList()
            else customerList.filter {
                "${it.name} ${it.lastName}".contains(query, ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _dishSearchQuery = MutableStateFlow("")
    val filteredDishes: StateFlow<List<Dish>> = _dishSearchQuery
        .combine(_dishes) { query, dishList ->
            if (query.isBlank()) emptyList()
            else dishList.filter { it.name.contains(query, ignoreCase = true) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadInitialData()
        loadDebts()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            customerUseCase.getAllCustomers().collect { customerList ->
                _customers.value = customerList
            }
        }
        viewModelScope.launch {
            dishUseCase.getAllDishes().collect { dishList ->
                _dishes.value = dishList
            }
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            combine(
                debtUseCase.getAllDebts(),
                customerUseCase.getAllCustomers()
            ) { debts, customers ->
                val customerMap = customers.associateBy { it.id }
                val groupedDebts = debts
                    .groupBy { it.customerId }
                    .mapNotNull { (customerId, customerDebts) ->
                        customerMap[customerId]?.let { customer ->
                            CustomerDebtGroup(
                                customer = customer,
                                debts = customerDebts
                            )
                        }
                    }
                    .sortedByDescending { it.totalAmount }

                _debtUiState.update { it.copy(
                    debts = debts,
                    customerDebtGroups = groupedDebts,
                    isSearching = false
                )}
            }.collect()
        }
    }

    fun updateCustomerSearch(query: String) {
        _customerSearchQuery.value = query
    }

    fun updateDishSearch(query: String) {
        _dishSearchQuery.value = query
    }

    fun updateSearchQuery(query: String) {
        _debtUiState.update { it.copy(
            searchQuery = query,
            isSearching = true
        )}

        viewModelScope.launch {
            combine(
                debtUseCase.getAllDebts(),
                customerUseCase.getAllCustomers()
            ) { debts, customers ->
                val customerMap = customers.associateBy { it.id }
                val filteredGroups = debts
                    .groupBy { it.customerId }
                    .mapNotNull { (customerId, customerDebts) ->
                        customerMap[customerId]?.let { customer ->
                            if ("${customer.name} ${customer.lastName}".contains(query, ignoreCase = true)) {
                                CustomerDebtGroup(
                                    customer = customer,
                                    debts = customerDebts
                                )
                            } else null
                        }
                    }
                    .sortedByDescending { it.totalAmount }

                _debtUiState.update { it.copy(
                    customerDebtGroups = filteredGroups,
                    isSearching = false
                )}
            }.collect()
        }
    }

    fun deleteDebt(debt: Debt) {
        viewModelScope.launch {
            debtUseCase.deleteDebt(debt)
            loadDebts()
        }
    }

    fun addDebtItem(dishId: Long, quantity: Int) {
        viewModelScope.launch {
            val dish = dishUseCase.getDishById(dishId) ?: return@launch
            val currentItems = _debtUiState.value.debtItems.toMutableList()
            val existingIndex = currentItems.indexOfFirst { it.first.id == dishId }

            if (existingIndex != -1) {
                currentItems[existingIndex] = currentItems[existingIndex].copy(
                    second = currentItems[existingIndex].second + quantity
                )
            } else {
                currentItems.add(Pair(dish, quantity))
            }

            _debtUiState.update { it.copy(
                debtItems = currentItems,
                totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
            )}
        }
    }

    fun removeDebtItem(index: Int) {
        val currentItems = _debtUiState.value.debtItems.toMutableList()
        currentItems.removeAt(index)
        _debtUiState.update { it.copy(
            debtItems = currentItems,
            totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
        )}
    }

    fun updateDebtItemQuantity(index: Int, newQuantity: Int) {
        if (newQuantity < 1) return
        val currentItems = _debtUiState.value.debtItems.toMutableList()
        currentItems[index] = currentItems[index].copy(second = newQuantity)
        _debtUiState.update { it.copy(
            debtItems = currentItems,
            totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
        )}
    }

    fun setSelectedCustomer(customer: Customer?) {
        _debtUiState.update { it.copy(selectedCustomer = customer) }
    }

    fun saveDebt() {
        viewModelScope.launch {
            val state = _debtUiState.value
            if (state.selectedCustomer == null || state.debtItems.isEmpty()) return@launch

            val description = state.debtItems.joinToString("\n") {
                "${it.first.name} x${it.second} ($${String.format("%.2f", it.first.price * it.second)})"
            }

            val debt = Debt(
                customerId = state.selectedCustomer.id,
                amount = state.totalAmount,
                dueDate = Date().time,
                description = description
            )

            debtUseCase.addDebt(debt)
            _debtUiState.update { DebtUiState() }
        }
    }

    fun clearDebtState() {
        _debtUiState.update { DebtUiState() }
    }
} 