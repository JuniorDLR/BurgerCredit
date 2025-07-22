package com.theburgerclub.burgercredit.presentation.debt.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.domain.model.ListItemUi
import com.theburgerclub.burgercredit.presentation.debt.model.DebtListItem
import com.theburgerclub.burgercredit.presentation.debt.viewmodel.DebtViewModel
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.shared.model.rememberTabResponsiveConfig
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared

@Composable
fun DebtsTab(
    navController: NavController,
    viewModel: DebtViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Debt Management") },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        DebtsTabBody(
            modifier = Modifier.padding(paddingValues = innerPadding),
            viewModel = viewModel,
            navController = navController
        )
    }
}

@Composable
fun DebtsTabBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DebtViewModel
) {

    val uiState by viewModel.debtUiState.collectAsState()
    val context = LocalContext.current
    var debtToDelete by remember { mutableStateOf<Debt?>(null) }
    val responsiveConfig = rememberTabResponsiveConfig()

    GenericListScreen<ListItemUi>(
        title = "Debts",
        searchPlaceholder = "Search debts",
        searchQuery = uiState.searchQuery,
        isSearching = uiState.isSearching,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        items = uiState.customerDebtGroups.map { DebtListItem(it) },
        onEdit = { item ->
            Toast.makeText(context, "Edit debts for: ${item.getTitle()}", Toast.LENGTH_SHORT).show()
        },
        onDelete = { item ->
            Toast.makeText(context, "Delete debts for: ${item.getTitle()}", Toast.LENGTH_SHORT)
                .show()
        },
        onDetails = { item ->
            Toast.makeText(context, "View details for: ${item.getTitle()}", Toast.LENGTH_SHORT)
                .show()
        },
        emptyMessage = "No debts registered yet",
        emptySubMessage = "Tap the + button to add your first debt!",
        showDeleteDialog = debtToDelete != null,
        itemToDelete = debtToDelete?.let { debt ->
            uiState.customerDebtGroups
                .firstOrNull { group -> group.debts.contains(debt) }
                ?.let { group -> DebtListItem(group) }
        },
        onConfirmDelete = {
            debtToDelete?.let { viewModel.deleteDebt(it) }
            Toast.makeText(context, "Debt deleted successfully", Toast.LENGTH_SHORT).show()
            debtToDelete = null
        },
        onDismissDelete = { debtToDelete = null },
        responsiveConfig = responsiveConfig,
        icon = Icons.Default.AttachMoney,
        modifier = modifier
    )
}