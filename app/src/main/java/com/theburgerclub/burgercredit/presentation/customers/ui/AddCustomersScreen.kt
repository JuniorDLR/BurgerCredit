package com.theburgerclub.burgercredit.presentation.customers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.customers.viewmodel.CustomerViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.theburgerclub.burgercredit.domain.model.Customer
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomersScreen(navController: NavController, viewModel: CustomerViewModel = hiltViewModel()) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

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

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = "Add Customer",
                showNavigationIcon = true,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.customerInput,
                    onValueChange = { viewModel.onCustomerInputChange(it) },
                    label = { Text("Name", fontSize = fontSize) },
                    placeholder = { Text("Enter customer name", fontSize = fontSize) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = uiState.customerInputError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = LoginColors.inputBackground,
                        focusedContainerColor = LoginColors.inputBackground,
                        disabledContainerColor = LoginColors.inputBackground,
                        unfocusedBorderColor = if (uiState.customerInputError != null) Color.Red else Color.Transparent,
                        focusedBorderColor = if (uiState.customerInputError != null) Color.Red else LoginColors.inputIcon,
                        disabledBorderColor = Color.Transparent,
                        cursorColor = LoginColors.dark,
                        focusedTextColor = LoginColors.dark,
                        unfocusedTextColor = LoginColors.dark,
                        disabledTextColor = LoginColors.inputIcon,
                        focusedPlaceholderColor = LoginColors.inputIcon,
                        unfocusedPlaceholderColor = LoginColors.inputIcon,
                        disabledPlaceholderColor = LoginColors.inputIcon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = textFieldMinHeight)
                )
                if (uiState.customerInputError != null) {
                    Text(
                        text = uiState.customerInputError ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(betweenFieldsSpace))
                Button(
                    onClick = {
                        scope.launch {
                            val success = viewModel.validateAndAddCustomer()
                            if (success) {
                                Toast.makeText(context, "Customer saved successfully!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = fontSize)
                    )
                }
            }
        }
    }
}