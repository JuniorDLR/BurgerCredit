package com.theburgerclub.burgercredit.presentation.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.R
import com.theburgerclub.burgercredit.presentation.home.model.HomeTab
import com.theburgerclub.burgercredit.presentation.customers.ui.CustomersTab
import com.theburgerclub.burgercredit.presentation.debt.ui.DebtsTab
import com.theburgerclub.burgercredit.presentation.dishes.ui.DishesTab
import com.theburgerclub.burgercredit.presentation.home.viewmodel.HomeViewModel
import com.theburgerclub.burgercredit.presentation.theme.BurgerOrange
import com.theburgerclub.burgercredit.presentation.theme.BurgerWhite
import com.theburgerclub.burgercredit.presentation.theme.BurgerBlack
import com.theburgerclub.burgercredit.presentation.shared.MainFabForTab
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.presentation.admin.ui.AdminTab
import com.theburgerclub.burgercredit.presentation.routes.AppRoute

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),

    ) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier,
        bottomBar = {
            MainBottomNavigation(
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::onTabSelected
            )
        },
        floatingActionButton = {
            MainFabForTab(selectedTab = uiState.selectedTab, onAdd = {
                when (uiState.selectedTab) {
                    HomeTab.CUSTOMERS -> navController.navigate(AppRoute.AddCustomerScreen.route)
                    HomeTab.DISHES -> navController.navigate(AppRoute.AddDishScreen.route)
                    HomeTab.DEBTS -> navController.navigate(AppRoute.AddDebtScreen.route)
                    else -> {}
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        MainContent(
            selectedTab = uiState.selectedTab,
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun MainBottomNavigation(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Responsive sizing
    val iconSize = when {
        screenWidth < 320 -> 20.dp  // Very small screens
        screenWidth < 480 -> 22.dp  // Small screens
        screenWidth > 720 -> 28.dp  // Large screens (tablets)
        else -> 24.dp               // Default
    }

    val fontSize = when {
        screenWidth < 320 -> 10.sp   // Very small screens
        screenWidth < 480 -> 11.sp   // Small screens
        screenWidth > 720 -> 14.sp   // Large screens (tablets)
        else -> 12.sp                // Default
    }

    val elevation = when {
        screenWidth > 720 -> 12.dp   // Large screens (tablets)
        else -> 8.dp                 // Default
    }

    BottomAppBar(
        containerColor = BurgerWhite,
        contentColor = BurgerBlack,
        tonalElevation = elevation
    ) {
        NavigationBarItem(
            selected = selectedTab == HomeTab.HOME,
            onClick = { onTabSelected(HomeTab.HOME) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    tint = if (selectedTab == HomeTab.HOME) BurgerOrange else BurgerBlack,
                    size = iconSize
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = fontSize,
                    fontWeight = if (selectedTab == HomeTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgerOrange,
                selectedTextColor = BurgerOrange,
                unselectedIconColor = BurgerBlack,
                unselectedTextColor = BurgerBlack,
                indicatorColor = BurgerWhite
            )
        )

        NavigationBarItem(
            selected = selectedTab == HomeTab.CUSTOMERS,
            onClick = { onTabSelected(HomeTab.CUSTOMERS) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.customers),
                    contentDescription = "Customers",
                    tint = if (selectedTab == HomeTab.CUSTOMERS) BurgerOrange else BurgerBlack,
                    size = iconSize
                )
            },
            label = {
                Text(
                    text = "Customers",
                    fontSize = fontSize,
                    fontWeight = if (selectedTab == HomeTab.CUSTOMERS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgerOrange,
                selectedTextColor = BurgerOrange,
                unselectedIconColor = BurgerBlack,
                unselectedTextColor = BurgerBlack,
                indicatorColor = BurgerWhite
            )
        )

        NavigationBarItem(
            selected = selectedTab == HomeTab.DISHES,
            onClick = { onTabSelected(HomeTab.DISHES) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Dishes",
                    tint = if (selectedTab == HomeTab.DISHES) BurgerOrange else BurgerBlack,
                    size = iconSize
                )
            },
            label = {
                Text(
                    text = "Dishes",
                    fontSize = fontSize,
                    fontWeight = if (selectedTab == HomeTab.DISHES) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgerOrange,
                selectedTextColor = BurgerOrange,
                unselectedIconColor = BurgerBlack,
                unselectedTextColor = BurgerBlack,
                indicatorColor = BurgerWhite
            )
        )

        NavigationBarItem(
            selected = selectedTab == HomeTab.DEBTS,
            onClick = { onTabSelected(HomeTab.DEBTS) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.debt),
                    contentDescription = "Debts",
                    tint = if (selectedTab == HomeTab.DEBTS) BurgerOrange else BurgerBlack,
                    size = iconSize
                )
            },
            label = {
                Text(
                    text = "Debts",
                    fontSize = fontSize,
                    fontWeight = if (selectedTab == HomeTab.DEBTS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgerOrange,
                selectedTextColor = BurgerOrange,
                unselectedIconColor = BurgerBlack,
                unselectedTextColor = BurgerBlack,
                indicatorColor = BurgerWhite
            )
        )

        NavigationBarItem(
            selected = selectedTab == HomeTab.ADMIN,
            onClick = { onTabSelected(HomeTab.ADMIN) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.admin),
                    contentDescription = "Admin",
                    tint = if (selectedTab == HomeTab.ADMIN) BurgerOrange else BurgerBlack,
                    size = iconSize
                )
            },
            label = {
                Text(
                    text = "Admin",
                    fontSize = fontSize,
                    fontWeight = if (selectedTab == HomeTab.ADMIN) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgerOrange,
                selectedTextColor = BurgerOrange,
                unselectedIconColor = BurgerBlack,
                unselectedTextColor = BurgerBlack,
                indicatorColor = BurgerWhite
            )
        )

    }
}

@Composable
fun HomeNavigationIcon(
    painter: Painter,
    contentDescription: String,
    tint: Color = Color.Unspecified,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}

@Composable
fun MainContent(
    selectedTab: HomeTab,
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    when (selectedTab) {
        HomeTab.HOME -> HomeTab()
        HomeTab.CUSTOMERS -> navController?.let { CustomersTab(navController = it) }
        HomeTab.DISHES -> navController?.let { DishesTab(navController = it) }
        HomeTab.DEBTS -> navController?.let { DebtsTab(navController = it) }
        HomeTab.ADMIN -> AdminTab()
    }
}