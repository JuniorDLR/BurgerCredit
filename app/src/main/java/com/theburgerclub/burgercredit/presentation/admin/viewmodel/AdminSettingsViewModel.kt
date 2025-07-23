package com.theburgerclub.burgercredit.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.domain.usecase.AdminUseCase
import com.theburgerclub.burgercredit.presentation.admin.model.AdminSettingsUiState
import com.theburgerclub.burgercredit.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSettingsViewModel @Inject constructor(
    private val adminUseCase: AdminUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminSettingsUiState())
    val uiState: StateFlow<AdminSettingsUiState> = _uiState.asStateFlow()

    private suspend fun getCurrentUsername(): String {
        return userPreferences.savedUsername.firstOrNull() ?: "admin"
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value, currentPasswordError = null) }
    }
    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value, newPasswordError = null) }
    }
    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }


    fun changePassword() {
        val state = _uiState.value
        if (state.currentPassword.isBlank() || state.newPassword.isBlank() || state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "All fields are required") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            return
        }
        if (state.currentPassword == state.newPassword) {
            _uiState.update { it.copy(newPasswordError = "New password must be different") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        viewModelScope.launch {
            delay(timeMillis = 1000)
            val username = getCurrentUsername()
            val admin = adminUseCase.authenticateAdmin(username, state.currentPassword)
            if (admin == null) {
                _uiState.update { it.copy(isLoading = false, currentPasswordError = "Current password is incorrect") }
                return@launch
            }
            try {
                val updatedAdmin = admin.copy(password = state.newPassword)
                adminUseCase.updateAdmin(updatedAdmin)
                _uiState.update { it.copy(isLoading = false, successMessage = "Password updated successfully", currentPassword = "", newPassword = "", confirmPassword = "") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error updating password: ${e.message}") }
            }
        }
    }
} 