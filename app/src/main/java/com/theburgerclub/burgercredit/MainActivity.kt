package com.theburgerclub.burgercredit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theburgerclub.burgercredit.presentation.customers.ui.AddCustomersScreen

import com.theburgerclub.burgercredit.presentation.home.ui.MainScreen
import com.theburgerclub.burgercredit.presentation.login.ui.LoginScreen
import com.theburgerclub.burgercredit.presentation.login.ui.SignUpScreen
import com.theburgerclub.burgercredit.presentation.routes.AppRoute
import com.theburgerclub.burgercredit.presentation.theme.BurgerCreditTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BurgerCreditTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph()
                }
            }
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoute.LoginScreen.route
    ) {
        composable(route = AppRoute.LoginScreen.route) { LoginScreen(navController) }
        composable(route = AppRoute.RegisterScreen.route) { SignUpScreen(navController) }
        composable(route = AppRoute.HomeScreen.route) { MainScreen(navController) }
        composable(route = AppRoute.AddCustomerScreen.route) { AddCustomersScreen(navController) }
        // composable(AppRoute.CustomerScreen.route) { ... }
        // composable(AppRoute.EditCustomerScreen.route) { ... }
        // composable(AppRoute.DishesScreen.route) { ... }
        // composable(AppRoute.EditDishScreen.route) { ... }
        // composable(AppRoute.DebtScreen.route) { ... }
        // composable(AppRoute.EditDebtScreen.route) { ... }
    }
}