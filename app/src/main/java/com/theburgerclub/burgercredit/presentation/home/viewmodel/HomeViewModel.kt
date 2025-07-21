package com.theburgerclub.burgercredit.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.presentation.home.model.HomeTab
import com.theburgerclub.burgercredit.presentation.home.model.HomeUiState
import com.theburgerclub.burgercredit.domain.usecase.CustomerUseCase
import com.theburgerclub.burgercredit.domain.usecase.DebtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase,
    private val debtUseCase: DebtUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    init {
        viewModelScope.launch {
            // Total customers
            customerUseCase.getAllCustomers().collect { customers ->
                _uiState.update { it.copy(totalCustomers = customers.size) }
            }
        }
        viewModelScope.launch {
            // Total active debts
            debtUseCase.getNumberOfActiveDebts().collect { count ->
                _uiState.update { it.copy(totalActiveDebts = count) }
            }
        }
        viewModelScope.launch {
            // Total customers with active debt
            debtUseCase.getTotalCustomersWithActiveDebt().collect { count ->
                _uiState.update { it.copy(totalCustomersWithActiveDebt = count) }
            }
        }
        viewModelScope.launch {
            // Total pending amount
            debtUseCase.getTotalPendingAmount().collect { amount ->
                _uiState.update { it.copy(totalPendingAmount = amount ?: 0.0) }
            }
        }
        // Top clients with more than 3 active debts
        viewModelScope.launch {
            combine(
                customerUseCase.getAllCustomers(),
                debtUseCase.getActiveDebts()
            ) { customers, debts ->
                // Map customerId to count of active debts
                val debtCountByCustomer = debts.groupBy { it.customerId }.mapValues { it.value.size }
                // Filter customers with more than 3 debts
                val top = customers
                    .filter { (debtCountByCustomer[it.id] ?: 0) > 3 }
                    .sortedByDescending { debtCountByCustomer[it.id] ?: 0 }
                    .take(3)
                    .map { "${it.name} ${it.lastName}" }
                top
            }.collect { topClients ->
                _uiState.update { it.copy(topClients = topClients) }
            }
        }
    }
} 