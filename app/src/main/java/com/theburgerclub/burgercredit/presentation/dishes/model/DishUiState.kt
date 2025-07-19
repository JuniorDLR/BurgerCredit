package com.theburgerclub.burgercredit.presentation.dishes.model

import com.theburgerclub.burgercredit.domain.model.Dish

data class DishUiState(
    val dishes: List<Dish> = emptyList(),
    val selectedDish: Dish? = null
) 