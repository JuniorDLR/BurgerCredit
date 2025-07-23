package com.theburgerclub.burgercredit.presentation.customers.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.theburgerclub.burgercredit.presentation.customers.model.Client
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.customers.viewmodel.CustomerViewModel
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.FabPosition
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.presentation.customers.model.ClientListItem
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.shared.model.rememberTabResponsiveConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import kotlinx.coroutines.flow.map


@Composable
fun CustomersTab(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Managing Clients") },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        CustomersBody(Modifier.padding(innerPadding), navController = navController)
    }
}


@Composable
fun CustomersBody(
    modifier: Modifier = Modifier,
    viewModel: CustomerViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    var clientToDelete by remember { mutableStateOf<Client?>(null) }
    val responsiveConfig = rememberTabResponsiveConfig()

    // Paging: mapeo Customer -> ClientListItem
    val customersPaging = viewModel.customersPaging
        .map { pagingData ->
            pagingData.map { customer ->
                ClientListItem(
                    Client(
                        name = customer.name,
                        lastName = customer.lastName,
                        icon = Icons.Default.Person,
                        debtsCount = uiState.customersDebtsCount[customer] ?: 0
                    )
                )
            }
        }
        .collectAsLazyPagingItems()

    GenericListScreen(
        title = "Customers",
        searchPlaceholder = "Search customers",
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = { query ->
            viewModel.updateSearchQuery(query = query)
        },
        pagingItems = customersPaging,
        onEdit = { item ->
            val client = item.client
            val customer = uiState.customers.find { it.name == client.name }
            customer?.let {
                viewModel.startEditCustomer(it)
                navController?.navigate("editCustomer/${it.id}")
            }
        },
        onDelete = { item ->
            val client = item.client
            val customer = uiState.customers.find { it.name == client.name }
            customer?.let {
                clientToDelete = client
            }
        },
        onDetails = { item ->
            val client = item.client
            val customer =
                uiState.customers.find { it.name == client.name && it.lastName == client.lastName }
            customer?.let {
                navController?.navigate("debtDetail/${it.id}")
            }
        },
        emptyMessage = "No customers registered yet",
        emptySubMessage = "Tap the + button to add your first customer!",
        showDeleteDialog = clientToDelete != null,
        itemToDelete = clientToDelete?.let { ClientListItem(it) },
        onConfirmDelete = {
            clientToDelete?.let { client ->
                uiState.customers.find { it.name == client.name }?.let { customer ->
                    viewModel.deleteCustomer(customer)
                    Toast.makeText(context, "Customer deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            clientToDelete = null
        },
        onDismissDelete = { clientToDelete = null },
        responsiveConfig = responsiveConfig,
        modifier = modifier
    )
}


