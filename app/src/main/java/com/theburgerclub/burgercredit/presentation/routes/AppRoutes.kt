package com.theburgerclub.burgercredit.presentation.routes

sealed class AppRoute(val route: String) {
    // Auth
    object LoginScreen : AppRoute("login")
    object RegisterScreen : AppRoute("register")
    
    // Home
    object HomeScreen : AppRoute("home")

    // Clientes
    object CustomerScreen : AppRoute("customer")
    object EditCustomerScreen : AppRoute("editCustomer/{customerId}")
    object AddCustomerScreen : AppRoute("addCustomer")

    // Platos
    object DishesScreen : AppRoute("dishes")
    object EditDishScreen : AppRoute("editDish/{dishId}")

    // Deudas
    object DebtScreen : AppRoute("debt")
    object EditDebtScreen : AppRoute("editDebt/{debtId}")
} 