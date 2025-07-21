package com.theburgerclub.burgercredit.presentation.customers.model

import androidx.compose.ui.graphics.vector.ImageVector


interface ListItemUi {
    fun getTitle(): String
    fun getIcon(): ImageVector
}

class ClientListItem(val client: Client) : ListItemUi {
    override fun getTitle() = client.getFullName()
    override fun getIcon() = client.icon
    fun getSubtitle(): String = if (client.debtsCount == 0) "No debts yet" else "${client.debtsCount} debts"
} 