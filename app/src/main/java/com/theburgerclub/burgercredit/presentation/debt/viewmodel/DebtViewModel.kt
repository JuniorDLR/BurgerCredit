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
import kotlinx.coroutines.delay
import com.theburgerclub.burgercredit.presentation.shared.formatCurrency
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.paging.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlin.collections.emptyList
import com.theburgerclub.burgercredit.presentation.debt.model.DebtListItem

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

    private val _editingDebt = MutableStateFlow<Debt?>(null)
    val editingDebt: StateFlow<Debt?> = _editingDebt.asStateFlow()


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


    val debts: StateFlow<List<Debt>> = debtUseCase.getAllDebts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _pagingSearchQuery = MutableStateFlow("")

    fun updatePagingSearchQuery(query: String) {
        _pagingSearchQuery.value = query
        _debtUiState.update { uiState ->
            uiState.copy(
                searchQuery = query
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val customersPaging = _pagingSearchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) customerUseCase.getCustomersPaging()
            else customerUseCase.searchCustomersPaging(query)
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    val customerDebtGroupsPaging = customersPaging
        .combine(debts) { pagingData, debtsList ->
            pagingData
                .map { customer: Customer ->
                    val activeDebts = debtsList.filter { it.customerId == customer.id && it.isActive }
                    DebtListItem(CustomerDebtGroup(customer, activeDebts))
                }
                .filter { item: DebtListItem ->
                    item.customerDebtGroup.debts.isNotEmpty()
                }
        }
        .cachedIn(viewModelScope)

    init {
        loadInitialData()

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
            delay(1000)
            combine(
                debtUseCase.getAllDebts(),
                customerUseCase.getAllCustomers()
            ) { debts, customers ->
                val customerMap = customers.associateBy { it.id }
                val groupedDebts = debts
                    .groupBy { it.customerId }
                    .mapNotNull { (customerId, customerDebts) ->
                        customerMap[customerId]?.let { customer ->
                            val activeDebts = customerDebts.filter { it.isActive }
                            if (activeDebts.isNotEmpty()) {
                                CustomerDebtGroup(
                                    customer = customer,
                                    debts = customerDebts
                                )
                            } else null
                        }
                    }
                    .sortedByDescending { it.totalAmount }

                _debtUiState.update {
                    it.copy(
                        debts = debts,
                        customerDebtGroups = groupedDebts,
                    )
                }
            }.collect()
        }
    }

    fun updateCustomerSearch(query: String) {
        _customerSearchQuery.value = query
    }

    fun updateDishSearch(query: String) {
        _dishSearchQuery.value = query
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

            _debtUiState.update {
                it.copy(
                    debtItems = currentItems,
                    totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
                )
            }
        }
    }

    fun removeDebtItem(index: Int) {
        val currentItems = _debtUiState.value.debtItems.toMutableList()
        currentItems.removeAt(index)
        _debtUiState.update {
            it.copy(
                debtItems = currentItems,
                totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
            )
        }
    }

    fun updateDebtItemQuantity(index: Int, newQuantity: Int) {
        if (newQuantity < 1) return
        val currentItems = _debtUiState.value.debtItems.toMutableList()
        currentItems[index] = currentItems[index].copy(second = newQuantity)
        _debtUiState.update {
            it.copy(
                debtItems = currentItems,
                totalAmount = currentItems.sumOf { item -> item.first.price * item.second }
            )
        }
    }

    fun setSelectedCustomer(customer: Customer?) {
        _debtUiState.update { it.copy(selectedCustomer = customer) }
    }

    fun saveDebt() {
        viewModelScope.launch {
            val state = _debtUiState.value
            if (state.selectedCustomer == null || state.debtItems.isEmpty()) return@launch

            val description = state.debtItems.joinToString("\n") {
                "${it.first.name} x${it.second} (${formatCurrency(it.first.price * it.second)})"
            }

            val debt = Debt(
                customerId = state.selectedCustomer.id,
                amount = state.totalAmount,
                dueDate = Date().time,
                description = description
            )

            debtUseCase.addDebt(debt)
            loadDebts()
            _debtUiState.update { DebtUiState() }
        }
    }

    fun clearDebtState() {
        _debtUiState.update { DebtUiState() }
    }

    fun loadDebtForEdit(debtId: Long?) {
        viewModelScope.launch {
            if (debtId == null) return@launch
            val debt = debtUseCase.getDebtById(debtId)
            _editingDebt.value = debt
            debt?.let {
                val customer =
                    customerUseCase.getAllCustomers().first().find { c -> c.id == debt.customerId }
                val allDishes = dishUseCase.getAllDishes().first()
                // Regex robusto para extraer todos los platos y cantidades
                val debtItemsMap = mutableMapOf<Dish, Int>()
                val lines = (debt.description ?: "").split("\n")
                // Acepta ($400.00), (C$400), ($400), (C$400.00), etc.
                val regex = Regex("""(.+?) x(\d+) \((?:C?\$)?([\d,.]+)\)""")
                for (line in lines) {
                    val match = regex.find(line.trim())
                    if (match != null) {
                        val dishName = match.groupValues[1].trim()
                        val quantity = match.groupValues[2].toIntOrNull() ?: 1
                        val dish = allDishes.find { it.name == dishName }
                        if (dish != null) {
                            debtItemsMap[dish] = (debtItemsMap[dish] ?: 0) + quantity
                        }
                    }
                }
                val debtItems = debtItemsMap.entries.map { it.key to it.value }
                _debtUiState.update { state ->
                    state.copy(
                        selectedCustomer = customer,
                        debtItems = debtItems,
                        totalAmount = debtItems.sumOf { it.first.price * it.second }
                    )
                }
            }
        }
    }

    fun updateDebt(debt: Debt, newAmount: Double, newDescription: String?) {
        viewModelScope.launch {
            val updatedDebt = debt.copy(amount = newAmount, description = newDescription)
            debtUseCase.updateDebt(updatedDebt)
            loadDebts()
        }
    }

    fun markAllDebtsAsPaid(customerDebtGroup: CustomerDebtGroup) {
        viewModelScope.launch {
            customerDebtGroup.debts.filter { it.isActive }.forEach { debt ->
                debtUseCase.updateDebtActiveStatus(debt.id, false)
            }
            loadDebts()
        }
    }
} 