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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@HiltViewModel
class DishViewModel @Inject constructor(
    private val dishUseCase: DishUseCase
) : ViewModel() {

    private val _dishUiState = MutableStateFlow(DishUiState())
    val dishUiState: StateFlow<DishUiState> = _dishUiState.asStateFlow()

    private var searchJob: Job? = null

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

    fun updateSearchQuery(query: String) {
        _dishUiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _dishUiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
        } else {
            searchJob = viewModelScope.launch {
                delay(300)
                searchDishes(query)
            }
        }
    }

    private suspend fun searchDishes(query: String) {
        _dishUiState.update { it.copy(isSearching = true) }
        delay(500)
        try {
            val dishes = dishUseCase.searchDishesByName(query).first()
            _dishUiState.update { it.copy(searchResults = dishes, isSearching = false) }
        } catch (e: Exception) {
            _dishUiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
        }
    }

    fun onDishNameInputChange(newValue: String) {
        _dishUiState.update { it.copy(dishNameInput = newValue, dishNameError = null) }
    }

    fun onPriceInputChange(newValue: String) {
        _dishUiState.update { it.copy(priceInput = newValue, priceError = null) }
    }

    fun clearErrors() {
        _dishUiState.update { it.copy(dishNameError = null, priceError = null) }
    }

     suspend fun validateAndAddDish(): Boolean {
        val name = _dishUiState.value.dishNameInput.trim()
        val priceStr = _dishUiState.value.priceInput.trim()
        var valid = true
        var nameError: String? = null
        var priceError: String? = null
        if (name.isBlank()) {
            nameError = "Name required"
            valid = false
        }
        val price = priceStr.toDoubleOrNull()
        if (priceStr.isBlank() || price == null) {
            priceError = "Valid price required"
            valid = false
        }
        // Verificar duplicado
        val existingDish = dishUseCase.getDishByName(name.lowercase())
        if (existingDish != null) {
            nameError = "A dish with this name already exists"
            valid = false
        }
        if (!valid) {
            _dishUiState.update { it.copy(dishNameError = nameError, priceError = priceError) }
            return false
        }
        _dishUiState.update { it.copy(isLoading = true) }
        addDish(Dish(name = name, price = price!!))
        _dishUiState.update { it.copy(dishNameInput = "", priceInput = "", isLoading = false) }
        return true
    }

    fun startEditDish(dish: Dish) {
        _dishUiState.update {
            it.copy(
                selectedDish = dish,
                dishNameInput = dish.name,
                priceInput = dish.price.toString(),
                dishNameError = null,
                priceError = null
            )
        }
    }

    suspend fun validateAndUpdateDish(): Boolean {
        val name = _dishUiState.value.dishNameInput.trim()
        val priceStr = _dishUiState.value.priceInput.trim()
        val selected = _dishUiState.value.selectedDish
        var valid = true
        var nameError: String? = null
        var priceError: String? = null
        if (name.isBlank()) {
            nameError = "Name required"
            valid = false
        }
        val price = priceStr.toDoubleOrNull()
        if (priceStr.isBlank() || price == null) {
            priceError = "Valid price required"
            valid = false
        }
        // Verificar duplicado (excepto el propio)
        val existingDish = dishUseCase.getDishByName(name.lowercase())
        if (existingDish != null && existingDish.id != selected?.id) {
            nameError = "A dish with this name already exists"
            valid = false
        }
        // No cambios
        if (selected != null && name == selected.name && price == selected.price) {
            _dishUiState.update {
                it.copy(
                    dishNameError = "No changes detected. Please modify the name or price.",
                    isLoading = false
                )
            }
            return false
        }
        if (!valid) {
            _dishUiState.update { it.copy(dishNameError = nameError, priceError = priceError) }
            return false
        }
        _dishUiState.update { it.copy(isLoading = true) }
        if (selected != null) {
            updateDish(selected.copy(name = name, price = price!!))
        }
        _dishUiState.update { it.copy(isLoading = false) }
        return true
    }

    suspend fun loadDishById(dishId: Long) {
        val dish = dishUseCase.getDishById(dishId)
        if (dish != null) {
            startEditDish(dish)
        }
    }
} 