package com.theburgerclub.burgercredit.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import com.theburgerclub.burgercredit.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    fun onRememberMeChange(remember: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = remember)
    }


} 