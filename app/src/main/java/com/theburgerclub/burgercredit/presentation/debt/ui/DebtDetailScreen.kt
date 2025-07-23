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
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailScreen(
    navController: NavController,
    customerId: Long?,
    viewModel: DebtViewModel = hiltViewModel()
) {
    val uiState by viewModel.debtUiState.collectAsState()
    val customerDebtGroup = uiState.customerDebtGroups.firstOrNull { it.customer.id == customerId }

    Scaffold(
        topBar = {
            TopAppBarShared(nameTopBar = "Debt Details", showNavigationIcon = true, onNavigationClick = { navController.popBackStack() })
        }
    ) { padding ->
        if (customerDebtGroup != null) {
            DebtDetailScreenBody(
                customerDebtGroup = customerDebtGroup,
                padding = padding,
                viewModel = viewModel,
                navController = navController
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No information found for this client.", color = Color.Gray)
            }
        }
    }
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
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        DebtSummaryCard(customerDebtGroup, activeDebts)
        Spacer(Modifier.height(18.dp))
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
            Spacer(Modifier.height(18.dp))
        }
        DebtListSection(activeDebts)
    }
}

@Composable
private fun DebtSummaryCard(customerDebtGroup: CustomerDebtGroup, activeDebts: List<Debt>) {
    val cliente = customerDebtGroup.customer
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
                text = "${cliente.name} ${cliente.lastName}",
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
                        text = "$${"%.2f".format(total)}",
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
    deuda: Debt,
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
                    text = dateFormat.format(Date(deuda.dueDate)),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF607D8B)
                )
                Text(
                    text = "$${"%.2f".format(deuda.amount)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                )
            }
            if (!deuda.description.isNullOrBlank()) {
                Text(
                    text = deuda.description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF607D8B)),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (deuda.isActive) "Status: Pending" else "Status: Paid",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (deuda.isActive) Color(0xFFE57373) else Color(0xFF43A047),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}