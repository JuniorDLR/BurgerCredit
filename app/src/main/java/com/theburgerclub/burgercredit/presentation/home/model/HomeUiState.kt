package com.theburgerclub.burgercredit.presentation.home.model

data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.HOME,
    val isLoading: Boolean = false,
    val totalCustomers: Int = 0,
    val totalActiveDebts: Int = 0,
    val totalCustomersWithActiveDebt: Int = 0,
    val totalPendingAmount: Double = 0.0,
    val topClients: List<String> = emptyList()
)

enum class HomeTab {
    HOME,
    CUSTOMERS,
    DISHES,
    DEBTS,
    ADMIN
} 