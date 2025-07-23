package com.theburgerclub.burgercredit.presentation.debt.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.ui.graphics.vector.ImageVector

import com.theburgerclub.burgercredit.domain.model.ListItemUi
import com.theburgerclub.burgercredit.presentation.shared.formatCurrency

data class DebtListItem(
    val customerDebtGroup: CustomerDebtGroup
) : ListItemUi {
    override fun getTitle(): String = "${customerDebtGroup.customer.name} ${customerDebtGroup.customer.lastName}"
    override fun getSubtitle(): String? = "${customerDebtGroup.activeDebtsCount} pending debts"
    override fun getIcon(): ImageVector = Icons.Default.AttachMoney
    fun getAmount(): String = formatCurrency(customerDebtGroup.totalAmount)
}