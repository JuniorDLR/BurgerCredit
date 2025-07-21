package com.theburgerclub.burgercredit.presentation.customers.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.theburgerclub.burgercredit.presentation.customers.model.Client
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.theburgerclub.burgercredit.presentation.shared.ActionSquareButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.customers.viewmodel.CustomerViewModel
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.theburgerclub.burgercredit.domain.model.Customer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.theburgerclub.burgercredit.presentation.customers.model.getFullName
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import androidx.compose.material3.FabPosition
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.presentation.customers.model.ClientListItem
import com.theburgerclub.burgercredit.presentation.shared.GenericListScreen
import com.theburgerclub.burgercredit.presentation.shared.model.rememberResponsiveConfig


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
fun CustomersBody(modifier: Modifier = Modifier, viewModel: CustomerViewModel = hiltViewModel(), navController: NavController? = null) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    // Estado para el cliente a eliminar
    var clientToDelete by remember { mutableStateOf<Client?>(null) }
    val clients = uiState.customers.map {
        Client(
            name = it.name,
            lastName = it.lastName,
            icon = Icons.Default.Person,
            debtsCount = uiState.customersDebtsCount[it] ?: 0
        )
    }
    val displayClients = if (uiState.searchQuery.isNotBlank()) {
        uiState.searchResults.map {
            Client(
                name = it.name,
                lastName = it.lastName,
                icon = Icons.Default.Person,
                debtsCount = uiState.customersDebtsCount[it] ?: 0
            )
        }
    } else {
        clients
    }
    fun getCustomerByName(name: String): Customer? = uiState.customers.find { it.name == name }
    GenericListScreen(
        modifier = modifier,
        title = if (uiState.searchQuery.isNotBlank()) "Search Results" else "Existing Clients",
        searchPlaceholder = "Search clients",
        searchQuery = uiState.searchQuery,
        isSearching = uiState.isSearching,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        items = displayClients.map { ClientListItem(it) },
        onEdit = { item ->
            val customer = getCustomerByName(item.client.name)
            if (customer != null && navController != null) {
                navController.navigate("editCustomer/${customer.id}")
            }
        },
        onDelete = { item -> clientToDelete = item.client },
        onDetails = null,
        emptyMessage = "No clients registered yet",
        emptySubMessage = "Tap the + button to add your first client!",
        showDeleteDialog = clientToDelete != null,
        itemToDelete = clientToDelete?.let { ClientListItem(it) },
        onConfirmDelete = {
            val customer = getCustomerByName(clientToDelete!!.name)
            if (customer != null) {
                viewModel.deleteCustomer(customer)
                Toast.makeText(context, "Customer deleted successfully", Toast.LENGTH_SHORT).show()
            }
            clientToDelete = null
        },
        onDismissDelete = { clientToDelete = null },
        getTitleForDialog = { it.client.getFullName() },
        icon = Icons.Default.Person,
        responsiveConfig = rememberResponsiveConfig()
    )
}

