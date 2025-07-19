package com.theburgerclub.burgercredit.presentation.login.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false
) 