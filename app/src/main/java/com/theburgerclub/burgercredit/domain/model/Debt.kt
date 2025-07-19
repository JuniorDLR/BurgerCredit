package com.theburgerclub.burgercredit.domain.model

data class Debt(
    val id: Long = 0,
    val customerId: Long,
    val amount: Double,
    val dueDate: Long,
    val isActive: Boolean = true,
    val description: String? = null
) 