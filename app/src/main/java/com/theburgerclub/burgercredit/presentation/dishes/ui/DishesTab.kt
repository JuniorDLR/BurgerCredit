package com.theburgerclub.burgercredit.presentation.dishes.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.domain.model.Dish
import com.theburgerclub.burgercredit.domain.model.ListItemUi
import com.theburgerclub.burgercredit.presentation.dishes.model.DishListItem
import com.theburgerclub.burgercredit.presentation.dishes.viewmodel.DishViewModel
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.shared.model.rememberTabResponsiveConfig
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared

@Composable
fun DishesTab(
    navController: NavController,
    viewModel: DishViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Managing Dishes") },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        DishesTabBody(
            modifier = Modifier.padding(paddingValues = innerPadding),
            viewModel = viewModel,
            navController = navController
        )
    }

}

@Composable
fun DishesTabBody(
    modifier: Modifier = Modifier,
    viewModel: DishViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    val uiState by viewModel.dishUiState.collectAsState()
    val context = LocalContext.current
    var dishToDelete by remember { mutableStateOf<Dish?>(null) }
    val responsiveConfig = rememberTabResponsiveConfig()

    GenericListScreen<ListItemUi>(
        title = "Dishes",
        searchPlaceholder = "Search dishes",
        searchQuery = uiState.searchQuery,
        isLoading = uiState.isLoading,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        items = if (uiState.searchQuery.isBlank()) {
            uiState.dishes.map { DishListItem(it) }
        } else {
            uiState.searchResults.map { DishListItem(it) }
        },
        onEdit = { item ->
            val dish = (item as DishListItem).dish
            navController?.navigate("editDish/${dish.id}")
        },
        onDelete = { item ->
            dishToDelete = (item as DishListItem).dish
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
        responsiveConfig = responsiveConfig,
        icon = Icons.Default.RestaurantMenu,
        modifier = modifier
    )
}