package com.theburgerclub.burgercredit.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.presentation.login.model.LoginUiState
import com.theburgerclub.burgercredit.presentation.login.model.LoginResultState
import com.theburgerclub.burgercredit.domain.usecase.AdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val adminUseCase: AdminUseCase
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _loginUiState.value = _loginUiState.value.copy(
            username = username,
            usernameError = null // Limpiar error específico cuando el usuario escribe
        )
    }
    
    fun onPasswordChange(password: String) {
        _loginUiState.value = _loginUiState.value.copy(
            password = password,
            passwordError = null // Limpiar error específico cuando el usuario escribe
        )
    }
    
    fun onRememberMeChange(remember: Boolean) {
        _loginUiState.value = _loginUiState.value.copy(rememberMe = remember)
    }

    fun onLogin() {
        val state = _loginUiState.value
        
        // Siempre mostrar loading cuando se hace clic en Login
        _loginUiState.value = _loginUiState.value.copy(
            result = LoginResultState.Loading
        )
        
        viewModelScope.launch {
            try {
                // Pequeño delay para mostrar el loading
                delay(2000)
                
                // Validar campos
                var hasErrors = false
                var usernameError: String? = null
                var passwordError: String? = null

                // Validar username
                if (state.username.isBlank()) {
                    usernameError = "Username cannot be empty"
                    hasErrors = true
                }

                // Validar password
                if (state.password.isBlank()) {
                    passwordError = "Password cannot be empty"
                    hasErrors = true
                } else if (state.password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    hasErrors = true
                }

                if (hasErrors) {
                    // Si hay errores de validación, mostrarlos
                    _loginUiState.value = _loginUiState.value.copy(
                        usernameError = usernameError,
                        passwordError = passwordError,
                        result = LoginResultState.Idle
                    )
                    return@launch
                }

                // Intentar autenticar con la base de datos
                val authenticatedAdmin = adminUseCase.authenticateAdmin(
                    username = state.username,
                    password = state.password
                )

                if (authenticatedAdmin != null) {
                    // Login exitoso
                    _loginUiState.value = _loginUiState.value.copy(
                        usernameError = null,
                        passwordError = null,
                        result = LoginResultState.Success
                    )
                    Log.d("LoginViewModel", "Login successful for user: ${authenticatedAdmin.username}")
                } else {
                    // Login fallido
                    _loginUiState.value = _loginUiState.value.copy(
                        usernameError = null,
                        passwordError = null,
                        result = LoginResultState.Error("Invalid username or password")
                    )
                    Log.d("LoginViewModel", "Login failed for username: ${state.username}")
                }
                
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error during login", e)
                _loginUiState.value = _loginUiState.value.copy(
                    result = LoginResultState.Error("Login failed. Please try again.")
                )
            }
        }
    }
} 