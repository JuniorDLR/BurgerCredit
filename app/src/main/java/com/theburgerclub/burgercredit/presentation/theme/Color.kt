package com.theburgerclub.burgercredit.presentation.theme

import androidx.compose.ui.graphics.Color

// Colores originales para compatibilidad con Theme.kt
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Paleta basada en el logo BurgerCredit
val BurgerOrange = Color(0xFFFFA726) // Naranja (pan y 'CREDIT')
val BurgerYellow = Color(0xFFFFEB3B) // Amarillo (queso)
val BurgerGreen = Color(0xFF43A047)  // Verde (lechuga)
val BurgerRed = Color(0xFFE53935)    // Rojo (tomate)
val BurgerBrown = Color(0xFF8D5524)  // Marrón (carne)
val BurgerWhite = Color(0xFFFFFFFF)  // Blanco (fondo y detalles)
val BurgerBlack = Color(0xFF232323)  // Negro (borde y texto)
val BurgerGray = Color(0xFFF2F5F9)   // Gris claro para fondos

// Colores para la app
val PrimaryColor = BurgerOrange
val SecondaryColor = BurgerYellow
val SuccessColor = BurgerGreen
val ErrorColor = BurgerRed
val BackgroundColor = BurgerWhite
val SurfaceColor = BurgerBlack
val OnPrimaryColor = BurgerWhite
val OnBackgroundColor = BurgerBlack
val InputBackground = BurgerGray
val InputIcon = Color(0xFFB0B8C1)
val ButtonPrimary = BurgerOrange
val ButtonSecondary = BurgerGray
val ButtonText = BurgerBlack
val LinkColor = BurgerRed  // Cambiado de BurgerGreen a BurgerRed

// Colores agrupados para login (puedes usar los globales directamente)
data object LoginColors {
    val logoBackground = PrimaryColor
    val background = BackgroundColor
    val inputBackground = InputBackground
    val inputIcon = InputIcon
    val buttonPrimary = ButtonPrimary
    val buttonSecondary = ButtonSecondary
    val buttonText = ButtonText
    val link = LinkColor  // Ahora será rojo
    val dark = OnBackgroundColor
} 