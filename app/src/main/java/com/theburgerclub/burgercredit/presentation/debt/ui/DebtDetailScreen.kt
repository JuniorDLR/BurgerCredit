package com.theburgerclub.burgercredit.presentation.debt.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.domain.model.Debt
import com.theburgerclub.burgercredit.presentation.debt.model.CustomerDebtGroup
import com.theburgerclub.burgercredit.presentation.debt.viewmodel.DebtViewModel
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailScreen(
    navController: NavController,
    customerId: Long?,
    viewModel: DebtViewModel = hiltViewModel()
) {
    val uiState by viewModel.debtUiState.collectAsState()
    val customerDebtGroup = uiState.customerDebtGroups.firstOrNull { it.customer.id == customerId }
    val isLoading = uiState.isLoading

    Scaffold(
        topBar = {
            TopAppBarShared(nameTopBar = "Debt Details", showNavigationIcon = true, onNavigationClick = { navController.popBackStack() })
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            customerDebtGroup != null -> {
                DebtDetailScreenBody(
                    customerDebtGroup = customerDebtGroup,
                    padding = padding,
                    viewModel = viewModel,
                    navController = navController
                )
            }
            else -> {
                EmptyDebtDetailsMessage(padding)
            }
        }
    }
}

private fun Double.toCordoba(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "NI"))
    return format.format(this)
}

@Composable
private fun DebtDetailScreenBody(
    customerDebtGroup: CustomerDebtGroup,
    padding: PaddingValues,
    viewModel: DebtViewModel = hiltViewModel(),
    navController: NavController? = null
) {
    val context = LocalContext.current
    val activeDebts = customerDebtGroup.debts.filter { it.isActive }
    val hasActiveDebts = activeDebts.isNotEmpty()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .widthIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DebtSummaryCard(customerDebtGroup, activeDebts)
        if (hasActiveDebts) {
            Button(
                onClick = {
                    viewModel.markAllDebtsAsPaid(customerDebtGroup)
                    Toast.makeText(context, "All debts marked as paid", Toast.LENGTH_SHORT).show()
                    navController?.popBackStack()
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mark all as Paid", color = Color.White)
            }
        }
        DebtListSection(activeDebts)
    }
}

@Composable
private fun DebtSummaryCard(customerDebtGroup: CustomerDebtGroup, activeDebts: List<Debt>) {
    val customer = customerDebtGroup.customer
    val total = activeDebts.sumOf { it.amount }
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6FA))
    ) {
        Column(Modifier.padding(24.dp)) {
            Text(
                text = "Client",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF607D8B)
            )
            Text(
                text = "${customer.name} ${customer.lastName}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = "Active debts",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF607D8B)
                    )
                    Text(
                        text = "${activeDebts.size}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total amount",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF607D8B)
                    )
                    Text(
                        text = total.toCordoba(),
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun DebtListSection(activeDebts: List<Debt>) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    Text(
        text = "Active debts details",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
    )
    if (activeDebts.isEmpty()) {
        Text("No active debts for this client.", color = Color.Gray, modifier = Modifier.padding(top = 12.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(activeDebts) { deuda ->
                DebtItemCard(deuda, dateFormat)
            }
        }
    }
}

@Composable
private fun DebtItemCard(
    debt: Debt,
    dateFormat: SimpleDateFormat
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = dateFormat.format(Date(debt.dueDate)),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF607D8B)
                )
                Text(
                    text = debt.amount.toCordoba(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                )
            }
            if (!debt.description.isNullOrBlank()) {
                Text(
                    text = debt.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF607D8B)),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (debt.isActive) "Status: Pending" else "Status: Paid",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (debt.isActive) Color(0xFFE57373) else Color(0xFF43A047),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun EmptyDebtDetailsMessage(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6FA)),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.widthIn(max = 400.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF90CAF9),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "No information found for this client.",
                    color = Color(0xFF607D8B),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "This client has no active debts or does not exist.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}