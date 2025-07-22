package com.theburgerclub.burgercredit.presentation.customers.model

import com.theburgerclub.burgercredit.domain.model.ListItemUi

class ClientListItem(val client: Client) : ListItemUi {
    override fun getTitle(): String = client.getFullName()
    override fun getSubtitle(): String? = if (client.debtsCount == 0) "No debts yet" else "${client.debtsCount} debts"
    override fun getIcon() = client.icon
} 