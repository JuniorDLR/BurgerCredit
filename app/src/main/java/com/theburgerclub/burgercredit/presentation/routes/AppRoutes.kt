package com.theburgerclub.burgercredit.presentation.routes

sealed class AppRoute(val route: String) {
    // Auth
    object LoginScreen : AppRoute("login")
    object RegisterScreen : AppRoute("register")
    
    // Home
    object HomeScreen : AppRoute("home")

    // Clientes

    object EditCustomerScreen : AppRoute("editCustomer/{customerId}")
    object AddCustomerScreen : AppRoute("addCustomer")

    // Platos
    object EditDishScreen : AppRoute("editDish/{dishId}")
    object AddDishScreen : AppRoute("addDishes")

    // Deudas
    object EditDebtScreen : AppRoute("editDebt/{debtId}")
} 