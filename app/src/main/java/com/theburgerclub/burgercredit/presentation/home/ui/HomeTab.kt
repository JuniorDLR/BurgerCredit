package com.theburgerclub.burgercredit.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.LottieConstants
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import com.theburgerclub.burgercredit.R
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    // Responsive sizing
    val contentPadding = when {
        screenWidth < 320 -> 12.dp  // Very small screens
        screenWidth < 480 -> 14.dp  // Small screens
        screenWidth > 720 -> 24.dp  // Large screens (tablets)
        else -> 16.dp               // Default
    }

    val spacing = when {
        screenHeight < 600 -> 16.dp   // Small screens
        screenHeight < 800 -> 20.dp   // Medium screens
        screenWidth > 720 -> 32.dp    // Large screens (tablets)
        else -> 24.dp                 // Default
    }

    Scaffold(
        topBar = { TopAppBarShared(nameTopBar = "Home") }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                top = contentPadding,
                start = contentPadding,
                end = contentPadding,
                bottom = contentPadding + 80.dp // Extra space for bottom navigation
            ),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            item {
                HomeBanner()
            }

            item {
                HomeSummarySection()
            }
        }
    }
}


@Composable
fun HomeBanner() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    val isLandscape = screenWidth > screenHeight

    // Responsive sizing
    val lottieSize = when {
        isLandscape && screenWidth > 720 -> 220.dp
        isLandscape -> 170.dp
        screenWidth < 360 -> 90.dp
        screenWidth < 480 -> 110.dp
        screenWidth > 720 -> 170.dp
        else -> 130.dp
    }
    val cardPadding = when {
        isLandscape && screenWidth > 720 -> 40.dp
        isLandscape -> 24.dp
        screenWidth < 360 -> 8.dp
        screenWidth < 480 -> 12.dp
        screenWidth > 720 -> 32.dp
        else -> 16.dp
    }
    val rowSpacing = when {
        isLandscape && screenWidth > 720 -> 48.dp
        isLandscape -> 32.dp
        screenWidth < 360 -> 8.dp
        screenWidth < 480 -> 12.dp
        screenWidth > 720 -> 32.dp
        else -> 16.dp
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isLandscape) Modifier.fillMaxHeight() else Modifier)
            .padding(cardPadding),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isLandscape) Modifier.fillMaxHeight() else Modifier)
                .padding(cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lottie a la izquierda
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.walking))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(lottieSize)
                        .fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.width(rowSpacing))
            // Texto a la derecha
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "BurgerCredit",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your space to manage credits",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun HomeSummarySection() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Responsive sizing
    val spacing = when {
        screenWidth < 320 -> 12.dp  // Very small screens
        screenWidth < 480 -> 14.dp  // Small screens
        screenWidth > 720 -> 20.dp  // Large screens (tablets)
        else -> 16.dp               // Default
    }

    val cardSpacing = when {
        screenWidth < 320 -> 8.dp   // Very small screens
        screenWidth < 480 -> 10.dp  // Small screens
        screenWidth > 720 -> 16.dp  // Large screens (tablets)
        else -> 12.dp               // Default
    }

    // Check if screen is in landscape mode
    val isLandscape = screenWidth > screenHeight

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (isLandscape) {
            // Single row layout for landscape
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(cardSpacing)
            ) {
                SummaryCard(
                    title = "Total Outstanding Debt",
                    value = "$5,400",
                    subtitle = "+$320 this month",
                    modifier = Modifier.weight(1f),
                    isPositive = true
                )

                SummaryCard(
                    title = "Total Customers",
                    value = "120",
                    subtitle = "With active debts",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Active Debts",
                    value = "150",
                    subtitle = "Pending payments",
                    modifier = Modifier.weight(1f)
                )
            }

        } else {
            // Two rows layout for portrait
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(cardSpacing)
            ) {
                SummaryCard(
                    title = "Total Outstanding Debt",
                    value = "$5,400",
                    subtitle = "+$320 this month",
                    modifier = Modifier.weight(1f),
                    isPositive = true
                )

                SummaryCard(
                    title = "Total Customers",
                    value = "120",
                    subtitle = "With active debts",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(cardSpacing)
            ) {
                val topClients = emptyList<String>() // tu lógica real
                ActiveDebtsCard(
                    activeDebts = "150",
                    subtitle = "Pending payments",
                    topClients = topClients,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    isPositive: Boolean = false,
    isWarning: Boolean = false
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Responsive sizing
    val padding = when {
        screenWidth < 320 -> 12.dp  // Very small screens
        screenWidth < 480 -> 14.dp  // Small screens
        screenWidth > 720 -> 20.dp  // Large screens (tablets)
        else -> 16.dp               // Default
    }

    val titleFontSize = when {
        screenWidth < 320 -> 12.sp   // Very small screens
        screenWidth < 480 -> 13.sp   // Small screens
        screenWidth > 720 -> 16.sp   // Large screens (tablets)
        else -> 14.sp                // Default
    }

    val valueFontSize = when {
        screenHeight < 600 -> 18.sp   // Small screens
        screenHeight < 800 -> 20.sp   // Medium screens
        screenWidth > 720 -> 28.sp    // Large screens (tablets)
        else -> 22.sp                 // Default
    }

    val spacing = when {
        screenHeight < 600 -> 6.dp    // Small screens
        screenHeight < 800 -> 7.dp    // Medium screens
        screenWidth > 720 -> 12.dp    // Large screens (tablets)
        else -> 8.dp                  // Default
    }

    // Determine card colors based on status - Different from banner
    val cardColor = when {
        isWarning -> MaterialTheme.colorScheme.errorContainer
        isPositive -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val valueColor = when {
        isWarning -> MaterialTheme.colorScheme.onErrorContainer
        isPositive -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val subtitleColor = when {
        isWarning -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
        isPositive -> MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = titleFontSize
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(spacing))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = valueFontSize
                ),
                color = valueColor
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(spacing / 2))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = titleFontSize * 0.8f
                    ),
                    textAlign = TextAlign.Center,
                    color = subtitleColor
                )
            }
        }
    }
}

@Composable
fun ActiveDebtsCard(
    activeDebts: String,
    subtitle: String,
    topClients: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Info principal a la izquierda
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Active Debts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = activeDebts,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // Clientes destacados o mensaje a la derecha
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "Top clients",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                if (topClients.isEmpty()) {
                    Text(
                        text = "No top clients yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    topClients.take(3).forEach { client ->
                        Text(
                            text = client,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


