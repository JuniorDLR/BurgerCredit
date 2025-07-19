package com.theburgerclub.burgercredit.presentation.dishes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.domain.usecase.DishUseCase
import com.theburgerclub.burgercredit.presentation.dishes.model.DishUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishViewModel @Inject constructor(
    private val dishUseCase: DishUseCase
) : ViewModel() {

    private val _dishUiState = MutableStateFlow(DishUiState())
    val dishUiState: StateFlow<DishUiState> = _dishUiState.asStateFlow()

    init {
        loadDishes()
    }

    fun loadDishes() {
        viewModelScope.launch {
            dishUseCase.getAllDishes().collect { list ->
                _dishUiState.update { it.copy(dishes = list) }
            }
        }
    }

    fun addDish(dish: Dish) {
        viewModelScope.launch {
            dishUseCase.addDish(dish)
            loadDishes()
        }
    }

    fun updateDish(dish: Dish) {
        viewModelScope.launch {
            dishUseCase.updateDish(dish)
            loadDishes()
        }
    }

    fun deleteDish(dish: Dish) {
        viewModelScope.launch {
            dishUseCase.deleteDish(dish)
            loadDishes()
        }
    }

    fun selectDish(dish: Dish?) {
        _dishUiState.update { it.copy(selectedDish = dish) }
    }

    fun searchDishesByName(name: String) {
        viewModelScope.launch {
            dishUseCase.searchDishesByName(name).collect { list ->
                _dishUiState.update { it.copy(dishes = list) }
            }
        }
    }
} 