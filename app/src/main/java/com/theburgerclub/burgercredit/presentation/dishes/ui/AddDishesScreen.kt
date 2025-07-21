package com.theburgerclub.burgercredit.presentation.dishes.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.theburgerclub.burgercredit.presentation.shared.GenericFormScreen
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.dishes.viewmodel.DishViewModel
import kotlinx.coroutines.launch
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import com.theburgerclub.burgercredit.presentation.shared.model.FormFieldData
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AddDishesScreen(navController: NavHostController, dishId: Long? = null, viewModel: DishViewModel = hiltViewModel()) {
    val uiState by viewModel.dishUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Precargar datos si es edición
    LaunchedEffect(dishId) {
        if (dishId != null && uiState.selectedDish?.id != dishId) {
            viewModel.loadDishById(dishId)
        }
    }

    val isEdit = uiState.selectedDish != null && dishId != null
    val topBarTitle = if (isEdit) "Edit Dish" else "Add Dish"

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = topBarTitle,
                showNavigationIcon = true,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        GenericFormScreen(
            title = topBarTitle,
            subtitle = if (isEdit) "Edit dish details" else "Register a new dish",
            description = if (isEdit) "Update the information below and save your changes." else "Fill in the details below to add a new dish.",
            fields = listOf(
                FormFieldData(
                    value = uiState.dishNameInput,
                    onValueChange = {
                        viewModel.onDishNameInputChange(it)
                        if (uiState.dishNameError != null) viewModel.clearErrors()
                    },
                    label = "Dish Name",
                    placeholder = "Enter dish name",
                    isError = uiState.dishNameError != null,
                    errorMessage = uiState.dishNameError,
                    leadingIcon = Icons.Default.Fastfood,
                    onClear = { viewModel.onDishNameInputChange("") }
                ),
                FormFieldData(
                    value = uiState.priceInput,
                    onValueChange = {
                        viewModel.onPriceInputChange(it)
                        if (uiState.priceError != null) viewModel.clearErrors()
                    },
                    label = "Price",
                    placeholder = "Enter price",
                    isError = uiState.priceError != null,
                    errorMessage = uiState.priceError,
                    leadingIcon = null,
                    onClear = { viewModel.onPriceInputChange("") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            ),
            onSubmit = {
                scope.launch {
                    val success = if (isEdit) viewModel.validateAndUpdateDish() else viewModel.validateAndAddDish()
                    if (success) {
                        Toast.makeText(context, if (isEdit) "Dish updated!" else "Dish saved!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            },
            submitButtonText = if (isEdit) "Save Changes" else "Save Dish",
            isLoading = uiState.isLoading,
            topContent = {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = "Dish image",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}