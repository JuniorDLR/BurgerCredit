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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.navigation.NavController
import androidx.compose.material.icons.automirrored.filled.ArrowBack

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