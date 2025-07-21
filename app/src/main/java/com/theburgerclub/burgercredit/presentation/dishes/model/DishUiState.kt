package com.theburgerclub.burgercredit.presentation.dishes.model

import com.theburgerclub.burgercredit.domain.model.Dish
import android.net.Uri

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
    val searchResults: List<Dish> = emptyList(),
    val imageUri: Uri? = null,
    val imageError: ImageError = ImageError.None,
    val stepImageState: StepImageState = StepImageState.NONE
)
