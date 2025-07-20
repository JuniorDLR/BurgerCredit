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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomersScreen(navController: NavController, viewModel: CustomerViewModel = hiltViewModel()) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.customerInput,
                    onValueChange = { viewModel.onCustomerInputChange(it) },
                    label = { Text("Name") },
                    placeholder = { Text("Enter customer name") },
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
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.customerInputError != null) {
                    Text(
                        text = uiState.customerInputError ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
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
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}