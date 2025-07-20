package com.theburgerclub.burgercredit.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theburgerclub.burgercredit.presentation.login.model.SignUpUiState
import com.theburgerclub.burgercredit.presentation.login.model.SignUpResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                username = username,
                usernameError = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                password = password,
                passwordError = null
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _signUpUiState.update { currentState ->
            currentState.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null
            )
        }
    }

    fun onSignUp() {
        viewModelScope.launch {
            // Set loading state
            _signUpUiState.update { it.copy(result = SignUpResultState.Loading) }

            // Small delay to ensure UI updates
            delay(2000)

            // Validate fields
            val username = _signUpUiState.value.username
            val password = _signUpUiState.value.password
            val confirmPassword = _signUpUiState.value.confirmPassword

            var hasErrors = false

            // Username validation
            if (username.isBlank()) {
                _signUpUiState.update {
                    it.copy(
                        usernameError = "Username is required",
                        result = SignUpResultState.Error("Username is required")
                    )
                }
                hasErrors = true
            } else if (username.length < 3) {
                _signUpUiState.update {
                    it.copy(
                        usernameError = "Username must be at least 3 characters",
                        result = SignUpResultState.Error("Username must be at least 3 characters")
                    )
                }
                hasErrors = true
            }

            // Password validation
            if (password.isBlank()) {
                _signUpUiState.update {
                    it.copy(
                        passwordError = "Password is required",
                        result = SignUpResultState.Error("Password is required")
                    )
                }
                hasErrors = true
            } else if (password.length < 6) {
                _signUpUiState.update {
                    it.copy(
                        passwordError = "Password must be at least 6 characters",
                        result = SignUpResultState.Error("Password must be at least 6 characters")
                    )
                }
                hasErrors = true
            }

            // Confirm password validation
            if (confirmPassword.isBlank()) {
                _signUpUiState.update {
                    it.copy(
                        confirmPasswordError = "Please confirm your password",
                        result = SignUpResultState.Error("Please confirm your password")
                    )
                }
                hasErrors = true
            } else if (password != confirmPassword) {
                _signUpUiState.update {
                    it.copy(
                        confirmPasswordError = "Passwords do not match",
                        result = SignUpResultState.Error("Passwords do not match")
                    )
                }
                hasErrors = true
            }

            if (!hasErrors) {
                try {

                    // Simulate sign up process
                    delay(2000)
                    // Set success state
                    _signUpUiState.update {
                        it.copy(result = SignUpResultState.Success)
                    }
                } catch (e: Exception) {
                    // Handle error
                    _signUpUiState.update {
                        it.copy(result = SignUpResultState.Error("Sign up failed. Please try again."))
                    }
                }
            }
        }
    }

    fun isNotLoading(): Boolean {
        return _signUpUiState.value.result !is SignUpResultState.Loading
    }
}