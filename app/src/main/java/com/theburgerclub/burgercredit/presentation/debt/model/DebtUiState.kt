package com.theburgerclub.burgercredit.presentation.debt.model

import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.domain.model.Dish

data class DebtUiState(
    // Add Debt Screen State
    val selectedCustomer: Customer? = null,
    val debtItems: List<Pair<Dish, Int>> = emptyList(),
    val totalAmount: Double = 0.0,
    
    // Debts Tab State
    val debts: List<Debt> = emptyList(),
    val customerDebtGroups: List<CustomerDebtGroup> = emptyList(),
    val searchQuery: String = "",
    
    // Shared State
    val isLoading: Boolean = false,
    val error: String? = null
) 