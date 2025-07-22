package com.theburgerclub.burgercredit.presentation.debt.model

import com.theburgerclub.burgercredit.domain.model.Customer
import com.theburgerclub.burgercredit.domain.model.Debt

data class CustomerDebtGroup(
    val customer: Customer,
    val debts: List<Debt>,
    val totalAmount: Double = debts.sumOf { it.amount },
    val activeDebtsCount: Int = debts.count { it.isActive }
) 