package com.theburgerclub.burgercredit.presentation.customers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.customers.viewmodel.CustomerViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import com.theburgerclub.burgercredit.presentation.shared.SharedOutlinedTextField
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.shadow

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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

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

    // Responsive paddings y tamaños
    val horizontalPadding = when {
        screenWidth < 320 -> 8.dp
        screenWidth < 480 -> 16.dp
        screenWidth > 720 -> 48.dp
        else -> 24.dp
    }
    val isLandscape = screenWidth > screenHeight
    val verticalPadding = when {
        isLandscape -> 8.dp
        screenHeight < 600 -> 12.dp
        screenHeight < 800 -> 20.dp
        screenWidth > 720 -> 48.dp
        else -> 32.dp
    }
    val betweenFieldsSpace = if (isLandscape) 32.dp else verticalPadding
    val textFieldMinHeight = when {
        isLandscape -> 64.dp
        screenHeight < 600 -> 56.dp
        else -> 60.dp
    }
    val buttonHeight = when {
        screenHeight < 600 -> 44.dp
        screenHeight < 800 -> 48.dp
        else -> 56.dp
    }
    val fontSize = when {
        screenWidth < 320 -> 13.sp
        screenWidth < 480 -> 15.sp
        screenWidth > 720 -> 20.sp
        else -> 16.sp
    }

    val topBarTitle = if (uiState.isEdit) {
        "Edit Customer"
    } else {
        "Add Customer"
    }

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = topBarTitle,
                showNavigationIcon = true,
                onNavigationClick = {
                    viewModel.exitEditMode()
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF7F7F9),
                            Color(0xFFE3E6F3)
                        )
                    )
                )
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .fillMaxWidth()
                    .shadow(18.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0x22000000)),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    // Avatar/Icono grande
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (uiState.isEdit) "Edit customer details" else "Register a new customer",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (uiState.isEdit) "Update the information below and save your changes." else "Fill in the details below to add a new customer.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(bottom = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        Modifier.padding(vertical = 8.dp),
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
                    SharedOutlinedTextField(
                        value = uiState.customerInput,
                        onValueChange = {
                            viewModel.onCustomerInputChange(it)
                            if (uiState.customerInputError != null) {
                                viewModel.clearErrors()
                            }
                        },
                        label = "Name",
                        placeholder = "Enter customer name",
                        isError = uiState.customerInputError != null,
                        errorMessage = uiState.customerInputError,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = fontSize,
                        minHeight = textFieldMinHeight,
                        onClear = { viewModel.onCustomerInputChange("") },
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SharedOutlinedTextField(
                        value = uiState.lastNameInput,
                        onValueChange = {
                            viewModel.onLastNameInputChange(it)
                            if (uiState.lastNameInputError != null) {
                                viewModel.clearErrors()
                            }
                        },
                        label = "Last Name",
                        placeholder = "Enter customer last name",
                        isError = uiState.lastNameInputError != null,
                        errorMessage = uiState.lastNameInputError,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = fontSize,
                        minHeight = textFieldMinHeight,
                        onClear = { viewModel.onLastNameInputChange("") },
                        leadingIcon = Icons.Default.Badge
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                if (uiState.isEdit && uiState.selectedCustomer != null) {
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (uiState.isEdit) "Save Changes" else "Save Customer",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = fontSize)
                            )
                        }
                    }
                }
            }
        }
    }
}