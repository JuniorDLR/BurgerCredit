package com.theburgerclub.burgercredit.presentation.login.model

data class SignUpUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val result: SignUpResultState = SignUpResultState.Idle
) 