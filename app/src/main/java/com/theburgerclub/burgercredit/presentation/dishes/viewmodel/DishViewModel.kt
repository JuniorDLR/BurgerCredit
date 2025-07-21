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
import android.net.Uri
import android.content.Context
import android.database.Cursor
import android.provider.OpenableColumns
import java.io.File
import androidx.core.net.toUri
import java.io.InputStream
import java.io.OutputStream

@HiltViewModel
class DishViewModel @Inject constructor(
    private val dishUseCase: DishUseCase
) : ViewModel() {

    private val _dishUiState = MutableStateFlow(DishUiState())
    val dishUiState: StateFlow<DishUiState> = _dishUiState.asStateFlow()

    private var searchJob: Job? = null

    // Image picker state
    fun setImageUri(uri: Uri?) {
        _dishUiState.update { it.copy(imageUri = uri) }
    }
    fun setImageError(error: DishUiState.ImageError) {
        _dishUiState.update { it.copy(imageError = error) }
    }
    fun changeUploadImageState(state: DishUiState.StepImageState) {
        _dishUiState.update { it.copy(stepImageState = state) }
    }
    fun handleStepImageLoading() {
        val uri = _dishUiState.value.imageUri
        if (uri == null) {
            changeUploadImageState(DishUiState.StepImageState.NONE)
        } else {
            changeUploadImageState(DishUiState.StepImageState.LOADING)
            viewModelScope.launch {
                delay(1200)
                changeUploadImageState(DishUiState.StepImageState.IMAGE)
            }
        }
    }

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
        val photoUri = _dishUiState.value.imageUri?.toString()
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
        addDish(Dish(name = name, price = price!!, photoUri = photoUri))
        _dishUiState.update { it.copy(dishNameInput = "", priceInput = "", isLoading = false, imageUri = null, stepImageState = DishUiState.StepImageState.NONE) }
        return true
    }

    fun startEditDish(dish: Dish) {
        _dishUiState.update {
            it.copy(
                selectedDish = dish,
                dishNameInput = dish.name,
                priceInput = dish.price.toString(),
                dishNameError = null,
                priceError = null,
                imageUri = dish.photoUri?.toUri(),
                stepImageState = if (dish.photoUri.isNullOrBlank()) DishUiState.StepImageState.NONE else DishUiState.StepImageState.IMAGE
            )
        }
    }

    suspend fun validateAndUpdateDish(): Boolean {
        val name = _dishUiState.value.dishNameInput.trim()
        val priceStr = _dishUiState.value.priceInput.trim()
        val selected = _dishUiState.value.selectedDish
        val photoUri = _dishUiState.value.imageUri?.toString()
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
        if (selected != null && name == selected.name && price == selected.price && photoUri == selected.photoUri) {
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
            updateDish(selected.copy(name = name, price = price!!, photoUri = photoUri))
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

    fun isValidImageExtension(path: String): Boolean {
        val allowedExtensions = listOf("svg", "png", "webp", "jpg", "jpeg")
        val extension = path.substringAfterLast('.', "").lowercase()
        return extension in allowedExtensions
    }
    fun isUnderMaxSize(path: String, maxSizeMB: Int = 2): Boolean {
        val file = File(path)
        val maxSizeByte = maxSizeMB * 1_048_576
        return file.length() <= maxSizeByte
    }
    fun isValidImage(path: String): Boolean {
        return isValidImageExtension(path) && isUnderMaxSize(path)
    }
    fun uploadImage(uri: Uri?, context: Context) {
        if (uri == null) {
            setImageUri(null)
            setImageError(DishUiState.ImageError.Empty)
            changeUploadImageState(DishUiState.StepImageState.NONE)
            return
        }
        val persistentUri = copyImageToInternalStorage(context, uri)
        val path = persistentUri?.path ?: ""
        val extensionValid = isValidImageExtension(path)
        val sizeValid = isUnderMaxSize(path)
        val imageError = when {
            path.isEmpty() -> DishUiState.ImageError.Empty
            !extensionValid -> DishUiState.ImageError.ErrorExtension
            !sizeValid -> DishUiState.ImageError.ErrorSize
            else -> DishUiState.ImageError.None
        }
        if (isValidImage(path)) {
            setImageUri(persistentUri)
            setImageError(DishUiState.ImageError.None)
            handleStepImageLoading()
        } else {
            setImageUri(null)
            setImageError(imageError)
            changeUploadImageState(DishUiState.StepImageState.NONE)
        }
    }

    private fun copyImageToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val ext = getFileExtension(context, uri)
            val fileName = "dish_${System.currentTimeMillis()}.$ext"
            val dir = File(context.filesDir, "dishes")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            val outputStream: OutputStream = file.outputStream()
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.toUri()
        } catch (e: Exception) {
            null
        }
    }

    private fun getFileExtension(context: Context, uri: Uri): String {
        var ext: String? = null
        if (uri.scheme == "content") {
            val mime = context.contentResolver.getType(uri)
            ext = android.webkit.MimeTypeMap.getSingleton().getExtensionFromMimeType(mime)
        }
        if (ext == null) {
            ext = uri.path?.substringAfterLast('.', "")
        }
        return ext ?: "jpg"
    }
} 