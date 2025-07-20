package com.theburgerclub.burgercredit.presentation.home.model

data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.HOME,
    val isLoading: Boolean = false
)

enum class HomeTab {
    HOME,
    CUSTOMERS,
    DISHES,
    DEBTS
} 