package com.theburgerclub.burgercredit.presentation.debt.model

import com.theburgerclub.burgercredit.domain.model.Debt

data class DebtUiState(
    val debts: List<Debt> = emptyList(),
    val selectedDebt: Debt? = null
) 