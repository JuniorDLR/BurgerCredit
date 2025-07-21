package com.theburgerclub.burgercredit.presentation.customers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.customers.viewmodel.CustomerViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.Icons
import com.theburgerclub.burgercredit.presentation.shared.GenericFormScreen
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import com.theburgerclub.burgercredit.presentation.shared.model.FormFieldData
import com.theburgerclub.burgercredit.presentation.customers.model.CustomerUiState
import kotlinx.coroutines.CoroutineScope

@Composable
 fun addCustomerFormFields(
    uiState: CustomerUiState,
    viewModel: CustomerViewModel
): List<FormFieldData> = listOf(
    FormFieldData(
        value = uiState.customerInput,
        onValueChange = {
            viewModel.onCustomerInputChange(it)
            if (uiState.customerInputError != null) viewModel.clearErrors()
        },
        label = "Name",
        placeholder = "Enter customer name",
        isError = uiState.customerInputError != null,
        errorMessage = uiState.customerInputError,
        leadingIcon = Icons.Default.Person,
        onClear = { viewModel.onCustomerInputChange("") }
    ),
    FormFieldData(
        value = uiState.lastNameInput,
        onValueChange = {
            viewModel.onLastNameInputChange(it)
            if (uiState.lastNameInputError != null) viewModel.clearErrors()
        },
        label = "Last Name",
        placeholder = "Enter customer last name",
        isError = uiState.lastNameInputError != null,
        errorMessage = uiState.lastNameInputError,
        leadingIcon = Icons.Default.Badge,
        onClear = { viewModel.onLastNameInputChange("") }
    )
)

@Composable
private fun AddCustomerTopContent() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(48.dp)
    )
}

@Composable
private fun AddCustomerForm(
    uiState: CustomerUiState,
    viewModel: CustomerViewModel,
    navController: NavController,
    responsiveConfig: com.theburgerclub.burgercredit.presentation.shared.model.FormResponsiveConfig,
    isEdit: Boolean,
    context: android.content.Context,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val topBarTitle = if (isEdit) "Edit Customer" else "Add Customer"
    GenericFormScreen(
        title = topBarTitle,
        subtitle = if (isEdit) "Edit customer details" else "Register a new customer",
        description = if (isEdit) "Update the information below and save your changes." else "Fill in the details below to add a new customer.",
        fields = addCustomerFormFields(uiState, viewModel),
        onSubmit = {
            scope.launch {
                if (isEdit && uiState.selectedCustomer != null) {
                    val success = viewModel.validateAndUpdateCustomer()
                    if (success) {
                        Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show()
                        viewModel.exitEditMode()
                        navController.popBackStack()
                    }
                } else {
                    val success = viewModel.validateAndAddCustomer()
                    if (success) {
                        Toast.makeText(context, "Customer saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            }
        },
        submitButtonText = if (isEdit) "Save Changes" else "Save Customer",
        isLoading = uiState.isLoading,
        topContent = { AddCustomerTopContent() },
        modifier = modifier,
        contentPadding = PaddingValues(responsiveConfig.horizontalPadding),
        cardPadding = PaddingValues(
            top = responsiveConfig.verticalPadding,
            start = responsiveConfig.horizontalPadding,
            end = responsiveConfig.horizontalPadding,
            bottom = responsiveConfig.verticalPadding
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomersScreen(
    navController: NavController,
    customerId: Long? = null,
    viewModel: CustomerViewModel = hiltViewModel()
) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val responsiveConfig = com.theburgerclub.burgercredit.presentation.shared.model.rememberFormResponsiveConfig()

    // Esperar a que el cliente esté disponible si es edición
    LaunchedEffect(customerId, uiState.customers) {
        if (customerId != null && !uiState.isEdit) {
            val customer = uiState.customers.find { it.id == customerId }
            if (customer != null) {
                viewModel.startEditCustomer(customer)
            }
        }
    }

    // Si es edición y el cliente aún no está cargado, mostrar loader
    if (customerId != null && (uiState.selectedCustomer == null || !uiState.isEdit)) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Precargar datos si es edición
    LaunchedEffect(uiState.isEdit, uiState.selectedCustomer) {
        val customer = uiState.selectedCustomer
        if (uiState.isEdit && customer != null) {
            viewModel.onCustomerInputChange(customer.name)
            viewModel.onLastNameInputChange(customer.lastName)
        }
    }

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = if (uiState.isEdit) "Edit Customer" else "Add Customer",
                showNavigationIcon = true,
                onNavigationClick = {
                    viewModel.exitEditMode()
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        AddCustomerForm(
            uiState = uiState,
            viewModel = viewModel,
            navController = navController,
            responsiveConfig = responsiveConfig,
            isEdit = uiState.isEdit,
            context = context,
            scope = scope,
            modifier = Modifier.padding(innerPadding)
        )
    }
}