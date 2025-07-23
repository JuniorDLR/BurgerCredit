import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.debt.viewmodel.DebtViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import java.text.SimpleDateFormat
import java.util.*
import com.theburgerclub.burgercredit.presentation.shared.formatCurrency
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.scale
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import com.theburgerclub.burgercredit.domain.model.Dish
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast


@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun CustomerSelectedCard(
    name: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onClear, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Clear, contentDescription = "Clear customer")
            }
        }
    }
}

@Composable
private fun DishSelectedCard(
    name: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.Fastfood, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.width(8.dp))
            Text(name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onClear, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Clear, contentDescription = "Clear dish")
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = onDecrease,
            enabled = quantity > 1,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease")
        }
        Box(
            modifier = Modifier.width(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                quantity.toString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
        IconButton(
            onClick = onIncrease,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase")
        }
    }
}

@Composable
private fun AddedDishItem(
    name: String,
    unitPrice: Double,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp), // Más padding horizontal
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp) // Padding generoso
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "${formatCurrency(unitPrice)} each",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuantitySelector(
                    quantity = quantity,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = formatCurrency(unitPrice * quantity),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(
                    onClick = onRemove,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun AddedDishesList(
    dishes: List<Triple<String, Double, Int>>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    // Efecto para hacer scroll al último elemento cuando se añade uno nuevo
    LaunchedEffect(dishes.size) {
        if (dishes.isNotEmpty()) {
            listState.animateScrollToItem(dishes.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 220.dp)
            .padding(vertical = 4.dp)
    ) {
        itemsIndexed(dishes) { index, (name, price, quantity) ->
            AddedDishItem(
                name = name,
                unitPrice = price,
                quantity = quantity,
                onIncrease = { onIncrease(index) },
                onDecrease = { onDecrease(index) },
                onRemove = { onRemove(index) }
            )
        }
    }
}

@Composable
private fun SearchResultIcon(
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = iconColor.copy(alpha = 0.1f),
        modifier = modifier.size(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    mainText: String,
    secondaryText: String?,
    icon: ImageVector?,
    iconColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else 
            MaterialTheme.colorScheme.surface,
        label = "backgroundColorAnimation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.98f else 1f,
        label = "scaleAnimation"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (icon != null) {
                SearchResultIcon(
                    icon = icon,
                    iconColor = iconColor
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (secondaryText != null) {
                    Text(
                        text = secondaryText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySearchResults(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No results found",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun <T> SearchResultsList(
    items: List<T>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Triple<String, String?, ImageVector?>,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 220.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val (mainText, secondaryText, icon) = itemContent(item)
            SearchResultItem(
                mainText = mainText,
                secondaryText = secondaryText,
                icon = icon,
                iconColor = iconColor,
                isSelected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onSelect(item)
                }
            )
        }
        
        if (items.isEmpty()) {
            item {
                EmptySearchResults()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDebtScreen(
    navController: NavHostController,
    viewModel: DebtViewModel = hiltViewModel(),
    debtId: Long? = null
) {
    val uiState by viewModel.debtUiState.collectAsState()
    val filteredCustomers by viewModel.filteredCustomers.collectAsState()
    val filteredDishes by viewModel.filteredDishes.collectAsState()

    // Soporte para edición
    val isEdit = debtId != null
    LaunchedEffect(debtId) {
        if (isEdit) {
            viewModel.loadDebtForEdit(debtId)
        }
    }

    var clienteSearch by remember { mutableStateOf("") }
    var platoSearch by remember { mutableStateOf("") }
    var cantidad by remember { mutableIntStateOf(1) }
    var platoSeleccionado by remember { mutableStateOf<Dish?>(null) }

    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val fechaActual = remember { Date() }

    val puedeGuardar = uiState.selectedCustomer != null && uiState.debtItems.isNotEmpty()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = if (isEdit) "Edit Debt" else "Add Debt",
                showNavigationIcon = true,
                onNavigationClick = {
                    viewModel.clearDebtState()
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY = 1200f,
                        tileMode = TileMode.Clamp
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 600.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEdit) "Edit Debt" else "Add Debt",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider(
                            Modifier.padding(vertical = 8.dp),
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                        // Customer selected or search
                        uiState.selectedCustomer?.let { customer ->
                            CustomerSelectedCard(
                                name = "${customer.name} ${customer.lastName}",
                                onClear = {
                                    viewModel.setSelectedCustomer(null)
                                    clienteSearch = ""
                                }
                            )
                        } ?: SearchField(
                            value = clienteSearch,
                            onValueChange = {
                                clienteSearch = it
                                viewModel.updateCustomerSearch(it)
                            },
                            label = "Search customer",
                            leadingIcon = Icons.Default.Person
                        )
                        // Dish selected or search
                        Spacer(Modifier.height(16.dp))
                        if (platoSeleccionado == null) {
                            SearchField(
                                value = platoSearch,
                                onValueChange = {
                                    platoSearch = it
                                    viewModel.updateDishSearch(it)
                                },
                                label = "Search dish",
                                leadingIcon = Icons.Default.Fastfood
                            )
                        } else {
                            DishSelectedCard(
                                name = platoSeleccionado!!.name,
                                onClear = { platoSeleccionado = null }
                            )
                            Spacer(Modifier.height(8.dp))
                            DishQuantitySelector(
                                quantity = cantidad,
                                onIncrease = { cantidad++ },
                                onDecrease = { if (cantidad > 1) cantidad-- },
                                onAdd = {
                                    if (platoSeleccionado != null) {
                                        viewModel.addDebtItem(
                                            dishId = platoSeleccionado!!.id,
                                            quantity = cantidad
                                        )
                                        platoSeleccionado = null
                                        cantidad = 1
                                        platoSearch = ""
                                    }
                                }
                            )
                        }
                    }
                }
                // Search results area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when {
                        clienteSearch.isNotBlank() && uiState.selectedCustomer == null -> {
                            SearchResultsList(
                                items = filteredCustomers,
                                onSelect = { customer ->
                                    viewModel.setSelectedCustomer(customer)
                                    clienteSearch = ""
                                },
                                iconColor = MaterialTheme.colorScheme.secondary,
                                itemContent = { customer ->
                                    Triple(
                                        "${customer.name} ${customer.lastName}",
                                        null, // No secondary info
                                        Icons.Default.Person
                                    )
                                }
                            )
                        }
                        platoSearch.isNotBlank() && platoSeleccionado == null -> {
                            SearchResultsList(
                                items = filteredDishes,
                                onSelect = { dish ->
                                    platoSeleccionado = dish
                                },
                                iconColor = MaterialTheme.colorScheme.secondary,
                                itemContent = { dish ->
                                    Triple(dish.name, formatCurrency(dish.price), Icons.Default.Fastfood)
                                }
                            )
                        }
                        uiState.debtItems.isNotEmpty() || uiState.isLoading -> {
                            Column {
                                Text(
                                    "Added dishes:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                if (uiState.isLoading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    AddedDishesList(
                                        dishes = uiState.debtItems.map { (dish, quantity) ->
                                            Triple(dish.name, dish.price, quantity)
                                        },
                                        onIncrease = { index ->
                                            viewModel.updateDebtItemQuantity(
                                                index,
                                                uiState.debtItems[index].second + 1
                                            )
                                        },
                                        onDecrease = { index ->
                                            viewModel.updateDebtItemQuantity(
                                                index,
                                                uiState.debtItems[index].second - 1
                                            )
                                        },
                                        onRemove = { index ->
                                            viewModel.removeDebtItem(index)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                // Footer with date and total
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        "Date: ${sdf.format(fechaActual)}",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Total: ${formatCurrency(uiState.totalAmount)}",
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (isEdit) {
                                val debt = viewModel.debtUiState.value.debts.find { it.id == debtId }
                                if (debt != null) {
                                    viewModel.updateDebt(
                                        debt = debt,
                                        newAmount = uiState.totalAmount,
                                        newDescription = uiState.debtItems.joinToString("\n") {
                                            "${it.first.name} x${it.second} (${formatCurrency(it.first.price * it.second)})"
                                        }
                                    )
                                    Toast.makeText(context, "Debt updated successfully", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                viewModel.saveDebt()
                            }
                            viewModel.clearDebtState()
                            navController.popBackStack()
                        },
                        enabled = puedeGuardar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(if (isEdit) "UPDATE DEBT" else "SAVE DEBT", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DishQuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            "Quantity:",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuantitySelector(
                quantity = quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease
            )
            Button(
                onClick = onAdd,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Add", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}