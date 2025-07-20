package com.theburgerclub.burgercredit.presentation.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainBottomNavigation(
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::onTabSelected
            )
        }
    ) { paddingValues ->
        MainContent(
            selectedTab = uiState.selectedTab,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun MainBottomNavigation(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    BottomAppBar(
        containerColor = BurgerWhite,
        contentColor = BurgerBlack,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == HomeTab.HOME,
            onClick = { onTabSelected(HomeTab.HOME) },
            icon = {
                HomeNavigationIcon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    tint = if (selectedTab == HomeTab.HOME) BurgerOrange else BurgerBlack
                )
            },
            label = { 
                Text(
                    text = "Home",
                    fontSize = 12.sp,
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
                    tint = if (selectedTab == HomeTab.CUSTOMERS) BurgerOrange else BurgerBlack
                )
            },
            label = { 
                Text(
                    text = "Customers",
                    fontSize = 12.sp,
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
                    tint = if (selectedTab == HomeTab.DISHES) BurgerOrange else BurgerBlack
                )
            },
            label = { 
                Text(
                    text = "Dishes",
                    fontSize = 12.sp,
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
                    painter = painterResource(id = R.drawable.deuda),
                    contentDescription = "Debts",
                    tint = if (selectedTab == HomeTab.DEBTS) BurgerOrange else BurgerBlack
                )
            },
            label = { 
                Text(
                    text = "Debts",
                    fontSize = 12.sp,
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
    }
}

@Composable
fun HomeNavigationIcon(
    painter: Painter,
    contentDescription: String,
    tint: Color = Color.Unspecified
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.size(24.dp),
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}

@Composable
fun MainContent(
    selectedTab: HomeTab,
    modifier: Modifier = Modifier
) {
    when (selectedTab) {
        HomeTab.HOME -> HomeTab()
        HomeTab.CUSTOMERS -> CustomersTab()
        HomeTab.DISHES -> DishesTab()
        HomeTab.DEBTS -> DebtsTab()
    }
}
