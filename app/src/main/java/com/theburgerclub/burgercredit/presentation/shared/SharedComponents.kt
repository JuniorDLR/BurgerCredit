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
import androidx.navigation.NavController
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge


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
    leadingIcon: ImageVector? = null
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
                .defaultMinSize(minHeight = minHeight)
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