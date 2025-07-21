package com.theburgerclub.burgercredit.presentation.dishes.model

import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.presentation.customers.model.ListItemUi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

class DishListItem(val dish: Dish) : ListItemUi {
    override fun getTitle() = dish.name
    override fun getIcon() = Icons.Default.Info
} 