package com.theburgerclub.burgercredit.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.presentation.login.model.LoginUiState
import com.theburgerclub.burgercredit.presentation.login.model.LoginResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
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
        
        // Simular verificación después de 3 segundos
        viewModelScope.launch {
            delay(3000) // Esperar 3 segundos
            
            // Después de 3 segundos, verificar si hay errores de validación
            var hasErrors = false
            var usernameError: String? = null
            var passwordError: String? = null

            // Validar username
            if (state.username.isBlank()) {
                usernameError = "Username cannot be empty"
                hasErrors = true
            }

            // Validar password
            when {
                state.password.isBlank() -> {
                    passwordError = "Password cannot be empty"
                    hasErrors = true
                }
                state.password.length < 8 -> {
                    passwordError = "Password must be at least 8 characters"
                    hasErrors = true
                }
            }

            if (hasErrors) {
                // Si hay errores de validación, mostrarlos
                _loginUiState.value = _loginUiState.value.copy(
                    usernameError = usernameError,
                    passwordError = passwordError,
                    result = LoginResultState.Idle
                )
            } else {
                // Si no hay errores de validación, simular error de login
                _loginUiState.value = _loginUiState.value.copy(
                    usernameError = null,
                    passwordError = null,
                    result = LoginResultState.Error("Invalid username or password")
                )
            }
        }
    }
} 