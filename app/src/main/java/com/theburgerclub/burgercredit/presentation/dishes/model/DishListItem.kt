package com.theburgerclub.burgercredit.presentation.dishes.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.ui.graphics.vector.ImageVector
import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.domain.model.ListItemUi

data class DishListItem(val dish: Dish) : ListItemUi {
    override fun getTitle(): String = dish.name
    override fun getIcon(): ImageVector = Icons.Default.RestaurantMenu
    override fun getSubtitle(): String? = "$${String.format("%.2f", dish.price)}"
    fun getPhotoUri(): String? = dish.photoUri
} 