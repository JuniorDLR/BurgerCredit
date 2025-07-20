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
            contentPadding = PaddingValues(contentPadding),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            item {
                HomeBanner()
            }
            
            item {
                HomeSummarySection()
            }
            
            item {
                HomeQuickActionsSection()
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
        
        // Summary cards row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(cardSpacing)
        ) {
            SummaryCard(
                title = "Total Customers with Debt",
                value = "120",
                modifier = Modifier.weight(1f)
            )
            
            SummaryCard(
                title = "Total Outstanding Debt",
                value = "$5,400",
                modifier = Modifier.weight(1f)
            )
        }
        
        // Full width card
        SummaryCard(
            title = "Active Debts",
            value = "150",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
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
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun HomeQuickActionsSection() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    
    // Responsive sizing
    val spacing = when {
        screenWidth < 320 -> 12.dp  // Very small screens
        screenWidth < 480 -> 14.dp  // Small screens
        screenWidth > 720 -> 20.dp  // Large screens (tablets)
        else -> 16.dp               // Default
    }
    
    val buttonSpacing = when {
        screenWidth < 320 -> 8.dp   // Very small screens
        screenWidth < 480 -> 10.dp  // Small screens
        screenWidth > 720 -> 16.dp  // Large screens (tablets)
        else -> 12.dp               // Default
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        val quickActions = listOf(
            "Manage Clients",
            "Manage Dishes", 
            "Record New Debt",
            "View Delinquent Clients"
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            quickActions.forEach { action ->
                QuickActionButton(
                    text = action,
                    onClick = {  }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onPrimary
        )
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