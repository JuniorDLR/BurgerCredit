package com.theburgerclub.burgercredit.presentation.login.model

sealed class SignUpResultState {
    object Idle : SignUpResultState()
    object Loading : SignUpResultState()
    object Success : SignUpResultState()
    data class Error(val message: String) : SignUpResultState()
} 