package com.theburgerclub.burgercredit.presentation.dishes.model

import com.theburgerclub.burgercredit.domain.model.Dish

data class DishUiState(
    val dishes: List<Dish> = emptyList(),
    val selectedDish: Dish? = null,
    val dishNameInput: String = "",
    val priceInput: String = "",
    val dishNameError: String? = null,
    val priceError: String? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<Dish> = emptyList()
) 