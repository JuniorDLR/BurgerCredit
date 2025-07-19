package com.theburgerclub.burgercredit.presentation.login.model

sealed class LoginResultState {
    object Idle : LoginResultState()
    object Loading : LoginResultState()
    object Success : LoginResultState()
    data class Error(val message: String) : LoginResultState()
} 