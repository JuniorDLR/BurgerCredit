package com.theburgerclub.burgercredit.presentation.customers.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomersTab() {
    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Managing Clients") },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End
    ) { innerPadding ->
        CustomersBody(Modifier.padding(innerPadding))
    }
}


@Composable
fun CustomersBody(modifier: Modifier = Modifier, viewModel: CustomerViewModel = hiltViewModel()) {
    val uiState by viewModel.customerUiState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = screenWidth > screenHeight

    // Responsive paddings y tamaños
    val horizontalPadding = when {
        screenWidth < 320 -> 8.dp
        screenWidth < 480 -> 16.dp
        screenWidth > 720 -> 48.dp
        else -> 20.dp
    }
    val verticalPadding = when {
        isLandscape -> 8.dp
        screenHeight < 600 -> 8.dp
        screenHeight < 800 -> 16.dp
        screenWidth > 720 -> 32.dp
        else -> 20.dp
    }
    val searchFieldHeight = when {
        isLandscape -> 56.dp
        screenHeight < 600 -> 48.dp
        else -> 52.dp
    }
    val cardHeight = when {
        screenHeight < 600 -> 56.dp
        screenHeight < 800 -> 64.dp
        else -> 70.dp
    }
    val fontSize = when {
        screenWidth < 320 -> 13.sp
        screenWidth < 480 -> 15.sp
        screenWidth > 720 -> 20.sp
        else -> 16.sp
    }

    val clients = uiState.customers.map {
        Client(
            name = it.name,
            icon = Icons.Default.Person
        )
    }
    fun getCustomerByName(name: String): Customer? = uiState.customers.find { it.name == name }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search clients", color = Color(0xFFB0B0B0), fontSize = fontSize) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalPadding)
                    .height(searchFieldHeight),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = Color(0xFFF7F7F9),
                    focusedContainerColor = Color(0xFFF7F7F9),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Existing Clients",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = fontSize),
                modifier = Modifier.padding(vertical = verticalPadding)
            )
        }
        ClientList(
            clients = clients,
            onEdit = {  },
            onDelete = { client ->
                val customer = getCustomerByName(client.name)
                if (customer != null) {
                    viewModel.deleteCustomer(customer)
                    Toast.makeText(context, "Customer deleted successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            cardHeight = cardHeight,
            fontSize = fontSize
        )
    }
}

@Composable
fun ClientCard(
    name: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cardHeight: Dp = 70.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, fontSize = fontSize)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableClientCard(
    name: String,
    icon: ImageVector,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    cardHeight: Dp = 70.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    val buttonWidth = 80.dp
    val actionsWidth = buttonWidth * 2
    val density = LocalDensity.current
    val maxSwipe = with(density) { actionsWidth.toPx() }
    val swipeOffset = remember { Animatable(0f) }
    var isRevealed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxWidth().height(cardHeight)) {
        // Fondo de acciones SOLO del ancho de los botones
        Row(
            modifier = Modifier
                .width(actionsWidth)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionSquareButton(
                modifier = Modifier.width(80.dp).fillMaxHeight(),
                icon = Icons.Default.Edit,
                label = "Editar",
                backgroundColor = Color(0xFFA5D6A7),
                onClick = {
                    onEdit()
                    scope.launch {
                        swipeOffset.animateTo(0f, tween(300))
                        isRevealed = false
                    }
                }
            )
            ActionSquareButton(
                modifier = Modifier.width(80.dp).fillMaxHeight(),
                icon = Icons.Default.Delete,
                label = "Eliminar",
                backgroundColor = Color(0xFFE57373),
                onClick = {
                    onDelete()
                    scope.launch {
                        swipeOffset.animateTo(0f, tween(300))
                        isRevealed = false
                    }
                }
            )
        }
        // Card deslizable (sin fondo ni borde extra)
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeOffset.value.toInt(), 0) }
                .fillMaxWidth()
                .height(cardHeight)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (swipeOffset.value < -maxSwipe / 2) {
                                    swipeOffset.animateTo(-maxSwipe, tween(200))
                                    isRevealed = true
                                } else {
                                    swipeOffset.animateTo(0f, tween(200))
                                    isRevealed = false
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = (swipeOffset.value + dragAmount).coerceIn(-maxSwipe, 0f)
                            scope.launch { swipeOffset.snapTo(newOffset) }
                        }
                    )
                }
                .pointerInput(isRevealed) {
                    if (isRevealed) {
                        detectTapGestures {
                            scope.launch {
                                swipeOffset.animateTo(0f, tween(200))
                                isRevealed = false
                            }
                        }
                    }
                }
        ) {
            ClientCard(name = name, icon = icon, cardHeight = cardHeight, fontSize = fontSize)
        }
    }
}

@Composable
fun ClientList(
    clients: List<Client>,
    onEdit: (Client) -> Unit,
    onDelete: (Client) -> Unit,
    cardHeight: Dp = 70.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    if (clients.isEmpty()) {
        EmptyClientsMessage(fontSize = fontSize)
    } else {
        ClientsLazyList(clients, onEdit, onDelete, cardHeight, fontSize)
    }
}

@Composable
fun EmptyClientsMessage(fontSize: androidx.compose.ui.unit.TextUnit = 16.sp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFFB0B0B0),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No clients registered yet",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFB0B0B0),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap the + button to add your first client!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFB0B0B0),
                    fontSize = fontSize
                )
            )
        }
    }
}

@Composable
fun ClientsLazyList(
    clients: List<Client>,
    onEdit: (Client) -> Unit,
    onDelete: (Client) -> Unit,
    cardHeight: Dp = 70.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    LazyColumn {
        items(clients) { client ->
            Column(modifier = Modifier.fillMaxWidth()) {
                SwipeableClientCard(
                    name = client.name,
                    icon = client.icon,
                    onEdit = { onEdit(client) },
                    onDelete = { onDelete(client) },
                    cardHeight = cardHeight,
                    fontSize = fontSize
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

