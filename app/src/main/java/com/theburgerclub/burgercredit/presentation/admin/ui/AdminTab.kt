package com.theburgerclub.burgercredit.presentation.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.admin.viewmodel.AdminSettingsViewModel
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        FieldError(error)
    }
}

@Composable
private fun FieldError(error: String?) {
    error?.let {
        Text(it, color = Color.Red, fontSize = 12.sp)
    }
}

@Composable
fun AdminTab(
    viewModel: AdminSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = "Admin Settings")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6FA))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Admin Settings", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(Modifier.height(20.dp))
                    PasswordField(
                        label = "Current Password",
                        value = uiState.currentPassword,
                        onValueChange = { viewModel.onCurrentPasswordChange(it) },
                        error = uiState.currentPasswordError
                    )
                    Spacer(Modifier.height(12.dp))
                    PasswordField(
                        label = "New Password",
                        value = uiState.newPassword,
                        onValueChange = { viewModel.onNewPasswordChange(it) },
                        error = uiState.newPasswordError
                    )
                    Spacer(Modifier.height(12.dp))
                    PasswordField(
                        label = "Confirm New Password",
                        value = uiState.confirmPassword,
                        onValueChange = { viewModel.onConfirmPasswordChange(it) },
                        error = uiState.confirmPasswordError
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.changePassword() },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Change Password", fontWeight = FontWeight.Bold)
                        }
                    }
                    uiState.successMessage?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(it, color = Color(0xFF43A047), fontWeight = FontWeight.SemiBold)
                    }
                    uiState.errorMessage?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(it, color = Color.Red, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
} 