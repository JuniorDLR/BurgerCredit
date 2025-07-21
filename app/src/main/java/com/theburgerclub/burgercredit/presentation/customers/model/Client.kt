package com.theburgerclub.burgercredit.presentation.customers.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Client(
    val name: String,
    val lastName: String,
    val icon: ImageVector,
    val debtsCount: Int = 0
)

fun Client.getFullName() = "$name $lastName" 