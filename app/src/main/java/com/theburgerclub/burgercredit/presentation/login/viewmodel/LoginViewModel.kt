package com.theburgerclub.burgercredit.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.presentation.login.model.LoginUiState
import com.theburgerclub.burgercredit.presentation.login.model.LoginResultState
import com.theburgerclub.burgercredit.domain.usecase.AdminUseCase
import com.theburgerclub.burgercredit.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val adminUseCase: AdminUseCase,
    private val userPreferences: UserPreferences
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

    fun loadSavedCredentials() {
        viewModelScope.launch {
            try {
                val rememberMe = userPreferences.rememberMe.first()
                val savedUsername = userPreferences.savedUsername.first()
                val savedPassword = userPreferences.savedPassword.first()
                
                // Solo marcar Remember Me si hay credenciales válidas guardadas
                val hasValidCredentials = rememberMe && savedUsername.isNotEmpty() && savedPassword.isNotEmpty()
                
                _loginUiState.value = _loginUiState.value.copy(
                    rememberMe = hasValidCredentials,
                    username = if (hasValidCredentials) savedUsername else "",
                    password = if (hasValidCredentials) savedPassword else ""
                )
                
                // Si hay credenciales guardadas, verificar automáticamente
                if (hasValidCredentials) {
                    verifySavedCredentials(savedUsername, savedPassword)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error loading saved credentials", e)
            }
        }
    }

    private fun verifySavedCredentials(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginUiState.value = _loginUiState.value.copy(
                    result = LoginResultState.Loading
                )
                
                // Delay más largo para mostrar el loading mejorado
                delay(3000)
                
                // Verificar credenciales con la base de datos
                val authenticatedAdmin = adminUseCase.authenticateAdmin(
                    username = username,
                    password = password
                )

                if (authenticatedAdmin != null) {
                    // Login automático exitoso
                    _loginUiState.value = _loginUiState.value.copy(
                        result = LoginResultState.Success
                    )
                    Log.d("LoginViewModel", "Auto-login successful for user: ${authenticatedAdmin.username}")
                } else {
                    // Credenciales inválidas, limpiar
                    userPreferences.clearCredentials()
                    _loginUiState.value = _loginUiState.value.copy(
                        result = LoginResultState.Idle,
                        rememberMe = false,
                        username = "",
                        password = ""
                    )
                    Log.d("LoginViewModel", "Auto-login failed, credentials cleared")
                }
                
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error during auto-login", e)
                _loginUiState.value = _loginUiState.value.copy(
                    result = LoginResultState.Idle
                )
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                // Limpiar credenciales guardadas
                userPreferences.clearCredentials()
                
                // Limpiar base de datos (eliminar admin)
                adminUseCase.deleteAllAdmins()
                
                // Resetear estado
                _loginUiState.value = LoginUiState()
                
                Log.d("LoginViewModel", "All data cleared successfully")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error clearing data", e)
            }
        }
    }

    fun onLogin() {
        val state = _loginUiState.value
        
        // Siempre mostrar loading cuando se hace clic en Login
        _loginUiState.value = _loginUiState.value.copy(
            result = LoginResultState.Loading
        )
        
        viewModelScope.launch {
            try {
                // Delay para mostrar el loading
                delay(2500)
                
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
                        result = LoginResultState.Success,
                        rememberMe = true, // Mantener marcado si estaba activado
                        rememberMeEnabled = false // Deshabilitar después del login exitoso
                    )
                    
                    // Guardar credenciales si Remember Me está activado
                    if (state.rememberMe) {
                        userPreferences.saveCredentials(state.username, state.password)
                        userPreferences.setRememberMe(true)
                    } else {
                        userPreferences.clearCredentials()
                    }
                    
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