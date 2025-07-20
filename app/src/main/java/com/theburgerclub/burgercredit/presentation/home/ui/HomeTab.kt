package com.theburgerclub.burgercredit.presentation.home.ui

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
        topBar = {
            HomeTopAppBar()
        }
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
    
    // Responsive sizing
    val bannerHeight = when {
        screenHeight < 600 -> 150.dp  // Small screens
        screenHeight < 800 -> 180.dp  // Medium screens
        screenWidth > 720 -> 250.dp   // Large screens (tablets)
        else -> 200.dp                // Default
    }
    
    val padding = when {
        screenWidth < 320 -> 12.dp  // Very small screens
        screenWidth < 480 -> 14.dp  // Small screens
        screenWidth > 720 -> 24.dp  // Large screens (tablets)
        else -> 16.dp               // Default
    }
    
    val fontSize = when {
        screenHeight < 600 -> 18.sp   // Small screens
        screenHeight < 800 -> 20.sp   // Medium screens
        screenWidth > 720 -> 28.sp    // Large screens (tablets)
        else -> 22.sp                 // Default
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .padding(padding),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Welcome to BurgerCredit",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
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
                
                SummaryCard(
                    title = "Overdue Debts",
                    value = "23",
                    subtitle = "Requires attention",
                    modifier = Modifier.weight(1f),
                    isWarning = true
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
                SummaryCard(
                    title = "Active Debts",
                    value = "150",
                    subtitle = "Pending payments",
                    modifier = Modifier.weight(1f)
                )
                
                SummaryCard(
                    title = "Overdue Debts",
                    value = "23",
                    subtitle = "Requires attention",
                    modifier = Modifier.weight(1f),
                    isWarning = true
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
} 