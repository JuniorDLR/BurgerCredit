package com.theburgerclub.burgercredit.presentation.dishes.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.dishes.viewmodel.DishViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.theburgerclub.burgercredit.presentation.dishes.model.DishListItem
import com.theburgerclub.burgercredit.presentation.shared.model.rememberTabResponsiveConfig
import androidx.navigation.NavController


@Composable
fun DishesTab(
    navController: NavController? = null
) {
    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Managing Dishes") },
    ) { innerPadding ->
        DishesTabBody(Modifier.padding(innerPadding), navController = navController)
    }
}

@Composable
fun DishesTabBody(modifier: Modifier = Modifier, viewModel: DishViewModel = hiltViewModel(), navController: NavController? = null) {
    val uiState by viewModel.dishUiState.collectAsState()
    val context = LocalContext.current

    var dishToDelete by remember { mutableStateOf<Dish?>(null) }
    val displayDishes = if (uiState.searchQuery.isNotBlank()) {
        uiState.searchResults
    } else {
        uiState.dishes
    }
    val responsiveConfig = rememberTabResponsiveConfig()
    GenericListScreen(
        title = "Dishes",
        searchPlaceholder = "Search dishes",
        searchQuery = uiState.searchQuery,
        isSearching = uiState.isSearching,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        items = displayDishes.map { DishListItem(it) },
        onEdit = { item ->
            navController?.navigate("editDish/${item.dish.id}")
        },
        onDelete = { item -> dishToDelete = item.dish },
        onDetails = { item ->
            Toast.makeText(context, "Detalles: ${item.dish.name}", Toast.LENGTH_SHORT).show()
        },
        emptyMessage = "No dishes registered yet",
        emptySubMessage = "Tap the + button to add your first dish!",
        showDeleteDialog = dishToDelete != null,
        itemToDelete = dishToDelete?.let { DishListItem(it) },
        onConfirmDelete = {
            dishToDelete?.let { viewModel.deleteDish(it) }
            Toast.makeText(context, "Dish deleted successfully", Toast.LENGTH_SHORT).show()
            dishToDelete = null
        },
        onDismissDelete = { dishToDelete = null },
        getTitleForDialog = { it.dish.name },
        responsiveConfig = responsiveConfig,
        modifier = modifier,
        icon = Icons.Default.Info
    )
}