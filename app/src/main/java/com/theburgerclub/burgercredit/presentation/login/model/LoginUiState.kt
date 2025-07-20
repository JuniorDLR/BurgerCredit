package com.theburgerclub.burgercredit.presentation.login.model

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val rememberMeEnabled: Boolean = true,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val result: LoginResultState = LoginResultState.Idle
) 