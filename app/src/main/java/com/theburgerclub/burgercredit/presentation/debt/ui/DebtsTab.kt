package com.theburgerclub.burgercredit.presentation.debt.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.presentation.debt.model.DebtListItem
import com.theburgerclub.burgercredit.presentation.debt.viewmodel.DebtViewModel
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.shared.model.rememberTabResponsiveConfig
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.paging.compose.collectAsLazyPagingItems
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

    val customerDebtGroupsPaging = viewModel.customerDebtGroupsPaging.collectAsLazyPagingItems()

    GenericListScreen(
        title = "Debts",
        searchPlaceholder = "Search debts",
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = { viewModel.updatePagingSearchQuery(it) },
        pagingItems = customerDebtGroupsPaging,
        onEdit = { item ->
            val debtListItem = item
            val activeDebt = debtListItem.customerDebtGroup.debts.firstOrNull { it.isActive }
            val debtId = activeDebt?.id
            if (debtId != null) {
                navController.navigate("editDebt/$debtId")
            }
        },
        onDelete = { item ->
            val debtListItem = item
            Toast.makeText(context, "Delete debts for: ${debtListItem.customerDebtGroup.customer.name}", Toast.LENGTH_SHORT).show()
        },
        onDetails = { item ->
            val debtListItem = item
            val customerId = debtListItem.customerDebtGroup.customer.id
            navController.navigate("debtDetail/$customerId")
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