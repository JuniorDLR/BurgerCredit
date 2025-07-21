package com.theburgerclub.burgercredit.presentation.shared

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.theburgerclub.burgercredit.presentation.home.model.HomeTab
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.theburgerclub.burgercredit.presentation.customers.ui.DeleteCustomerDialog
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import com.theburgerclub.burgercredit.presentation.customers.model.ListItemUi
import com.theburgerclub.burgercredit.presentation.shared.model.FormFieldData
import com.theburgerclub.burgercredit.presentation.shared.model.ResponsiveConfig


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarShared(
    nameTopBar: String,
    showNavigationIcon: Boolean = false,
    onNavigationClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = nameTopBar,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (showNavigationIcon && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

            } else null
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF3F4F6), // Gris claro más notorio
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.graphicsLayer {
            shadowElevation = 8f // Sombra sutil para mayor separación
        }
    )
}


@Composable
fun SharedExtendedFab(
    text: String,
    onAdd: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "fabScale")

    ExtendedFloatingActionButton(
        onClick = onAdd,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        containerColor = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(50),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 14.dp,
            pressedElevation = 20.dp,
            focusedElevation = 16.dp,
            hoveredElevation = 16.dp
        ),
        modifier = Modifier
            .scale(scale)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .graphicsLayer {
                shadowElevation = 24f
                shape = RoundedCornerShape(50)
                clip = true
            },
        interactionSource = interactionSource
    )
}

@Composable
fun MainFabForTab(
    selectedTab: HomeTab,
    onAdd: () -> Unit = {},
) {
    when (selectedTab) {
        HomeTab.CUSTOMERS -> SharedExtendedFab(text = "Add Client", onAdd = onAdd)
        HomeTab.DISHES -> SharedExtendedFab(text = "Add dishes", onAdd = onAdd)
        else -> {}
    }
}


@Composable
fun ActionSquareButton(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun SharedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    minHeight: Dp = 60.dp,
    onClear: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontSize = fontSize) },
            placeholder = { Text(placeholder, fontSize = fontSize) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = isError,
            leadingIcon = if (leadingIcon != null) {
                { Icon(imageVector = leadingIcon, contentDescription = null, tint = if (isError) Color.Red else MaterialTheme.colorScheme.primary) }
            } else null,
            trailingIcon = {
                if (onClear != null && value.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LoginColors.inputBackground,
                focusedContainerColor = LoginColors.inputBackground,
                disabledContainerColor = LoginColors.inputBackground,
                unfocusedBorderColor = if (isError) Color.Red else Color.Transparent,
                focusedBorderColor = if (isError) Color.Red else LoginColors.inputIcon,
                disabledBorderColor = Color.Transparent,
                cursorColor = LoginColors.dark,
                focusedTextColor = LoginColors.dark,
                unfocusedTextColor = LoginColors.dark,
                disabledTextColor = LoginColors.inputIcon,
                focusedPlaceholderColor = LoginColors.inputIcon,
                unfocusedPlaceholderColor = LoginColors.inputIcon,
                disabledPlaceholderColor = LoginColors.inputIcon,
                focusedLabelColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight),
            keyboardOptions = keyboardOptions
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun <T : ListItemUi> GenericListScreen(
    modifier: Modifier = Modifier,
    title: String,
    searchPlaceholder: String = "Search...",
    searchQuery: String,
    isSearching: Boolean,
    onSearchQueryChange: (String) -> Unit,
    items: List<T>,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    onDetails: ((T) -> Unit)? = null,
    emptyMessage: String = "No items registered yet",
    emptySubMessage: String = "Tap the + button to add your first item!",
    showDeleteDialog: Boolean,
    itemToDelete: T?,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    getTitleForDialog: (T) -> String = { it.getTitle() },
    responsiveConfig: ResponsiveConfig ,
    icon: ImageVector = Icons.Default.Person
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = responsiveConfig.horizontalPadding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        searchPlaceholder,
                        color = Color(0xFFB0B0B0),
                        fontSize = responsiveConfig.fontSize
                    )
                },
                leadingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = responsiveConfig.verticalPadding)
                    .height(responsiveConfig.searchFieldHeight),
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
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = responsiveConfig.titleFontSize
                ),
                modifier = Modifier.padding(vertical = responsiveConfig.verticalPadding)
            )
        }
        if (items.isEmpty()) {
            EmptyClientsMessage(
                fontSize = responsiveConfig.fontSize,
                message = emptyMessage,
                subMessage = emptySubMessage,
                icon = icon
            )
        } else {
            LazyColumn {
                items(items) { item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SwipeableClientCard(
                            name = item.getTitle(),
                            icon = item.getIcon(),
                            onEdit = { onEdit(item) },
                            onDelete = { onDelete(item) },
                            onDetails = onDetails?.let { { it(item) } },
                            cardHeight = responsiveConfig.cardHeight,
                            fontSize = responsiveConfig.fontSize
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFE0E0E0)
                        )
                    }
                }
            }
        }
    }
    if (showDeleteDialog && itemToDelete != null) {
        DeleteCustomerDialog(
            customerName = getTitleForDialog(itemToDelete),
            onConfirm = onConfirmDelete,
            onDismiss = onDismissDelete
        )
    }
}

@Composable
fun EmptyClientsMessage(
    fontSize: TextUnit = 16.sp,
    message: String = "No items registered yet",
    subMessage: String = "Tap the + button to add your first item!",
    icon: ImageVector = Icons.Default.Person
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFB0B0B0),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFFB0B0B0),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subMessage,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFB0B0B0),
                    fontSize = fontSize
                )
            )
        }
    }
}

// 6. SwipeableClientCard con onDetails opcional
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableClientCard(
    name: String,
    icon: ImageVector,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDetails: (() -> Unit)? = null,
    cardHeight: Dp = 70.dp,
    fontSize: TextUnit = 16.sp
) {
    val buttonWidth = 80.dp
    val actionsWidth = buttonWidth * 3
    val density = LocalDensity.current
    val maxSwipe = with(density) { actionsWidth.toPx() }
    val swipeOffset = remember { Animatable(0f) }
    var isRevealed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
    ) {
        Row(
            modifier = Modifier
                .width(actionsWidth)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionSquareButton(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
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
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                icon = Icons.Default.Info,
                label = "Detalles",
                backgroundColor = Color(0xFF90CAF9),
                onClick = {
                    onDetails?.invoke()
                    scope.launch {
                        swipeOffset.animateTo(0f, tween(300))
                        isRevealed = false
                    }
                }
            )
            ActionSquareButton(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
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
            // Puedes personalizar el card aquí si lo deseas
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
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
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = fontSize
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericFormScreen(
    title: String,
    subtitle: String,
    description: String,
    fields: List<FormFieldData>,
    onSubmit: () -> Unit,
    submitButtonText: String,
    isLoading: Boolean,
    topContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    buttonEnabled: Boolean = true,
    backgroundBrush: Brush = Brush.verticalGradient(listOf(Color(0xFFF7F7F9), Color(0xFFE3E6F3))),
    cardShape: RoundedCornerShape = RoundedCornerShape(28.dp),
    cardPadding: PaddingValues = PaddingValues(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
    contentPadding: PaddingValues = PaddingValues(28.dp)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(cardPadding)
                .fillMaxWidth()
                .shadow(18.dp, cardShape),
            shape = cardShape,
            border = BorderStroke(1.dp, Color(0x22000000)),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(contentPadding)
            ) {
                if (topContent != null) {
                    topContent()
                    Spacer(Modifier.height(12.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
                fields.forEachIndexed { idx, field ->
                    SharedOutlinedTextField(
                        value = field.value,
                        onValueChange = field.onValueChange,
                        label = field.label,
                        placeholder = field.placeholder,
                        isError = field.isError,
                        errorMessage = field.errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        minHeight = 60.dp,
                        onClear = field.onClear,
                        leadingIcon = field.leadingIcon,
                        keyboardOptions = field.keyboardOptions
                    )
                    if (idx < fields.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp),
                    enabled = buttonEnabled && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = submitButtonText,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}