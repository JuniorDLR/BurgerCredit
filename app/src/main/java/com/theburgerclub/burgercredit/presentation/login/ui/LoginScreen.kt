package com.theburgerclub.burgercredit.presentation.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.theburgerclub.burgercredit.R
import com.theburgerclub.burgercredit.presentation.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import com.theburgerclub.burgercredit.presentation.login.model.LoginResultState
import com.theburgerclub.burgercredit.presentation.routes.AppRoute
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.wrapContentHeight


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val context = LocalContext.current

    // Responsive: calcula screenWidth y screenHeight aquí
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val containerSize = windowInfo.containerSize
    val screenWidth = containerSize.width / density.density
    val screenHeight = containerSize.height / density.density

    LaunchedEffect(loginUiState.result) {
        when (val result = loginUiState.result) {
            is LoginResultState.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }

            is LoginResultState.Success -> {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LoginColors.background,
                        LoginColors.background.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        LoginBody(loginViewModel, navController)
        // AlertDialog para verificación automática
        if (loginUiState.result is LoginResultState.Loading && loginUiState.rememberMe) {
            AutoLoginDialog(screenWidth = screenWidth, screenHeight = screenHeight)
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginBody(viewModel: LoginViewModel = hiltViewModel(), navController: NavController) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    var animationStep by remember { mutableIntStateOf(0) }
    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize
    val density = LocalDensity.current
    val screenWidth = containerSize.width / density.density
    val screenHeight = containerSize.height / density.density

    // Lógica para habilitar controles solo cuando no está cargando
    val controlsEnabled = loginUiState.result !is LoginResultState.Loading

    // Load saved credentials on first launch
    LaunchedEffect(Unit) {
        viewModel.loadSavedCredentials()
    }

    // Handle navigation after successful login
    LaunchedEffect(loginUiState.result) {
        if (loginUiState.result is LoginResultState.Success) {
            delay(500) // Small delay to show success state
            navController.navigate(AppRoute.HomeScreen.route) {
                popUpTo(AppRoute.LoginScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        // Smooth staggered animation without fixed delays
        animationStep = 1
        delay(80)
        animationStep = 2
        delay(120)
        animationStep = 3
        delay(160)
        animationStep = 4
        delay(200)
        animationStep = 5
        delay(240)
        animationStep = 6
    }

    // Responsive spacing based on screen size
    val spacing = when {
        screenHeight < 600 -> 16.dp  // Small screens
        screenHeight < 800 -> 20.dp  // Medium screens
        else -> 22.dp                // Large screens
    }

    // Responsive horizontal padding
    val horizontalPadding = when {
        screenWidth < 320 -> 16.dp   // Very small screens
        screenWidth < 480 -> 20.dp   // Small screens
        screenWidth < 720 -> 24.dp   // Medium screens
        else -> 32.dp                // Large screens
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding)
            .imePadding()
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing, Alignment.Top)
    ) {
        item {
            AnimatedVisibility(
                visible = animationStep >= 1,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { -20 },
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                )
            ) { AppLogo(screenWidth, screenHeight) }
        }

        item {
            AnimatedVisibility(
                visible = animationStep >= 2,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { -15 },
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            ) { AppTitle(screenWidth, screenHeight) }
        }

        item {
            AnimatedVisibility(
                visible = animationStep >= 3,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { 20 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UsernameInput(
                        loginUiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        error = loginUiState.usernameError,
                        enabled = controlsEnabled,
                        screenHeight = screenHeight
                    )
                    PasswordInput(
                        loginUiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        error = loginUiState.passwordError,
                        enabled = controlsEnabled,
                        screenHeight = screenHeight
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = animationStep >= 4,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { 15 },
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                )
            ) {
                RememberMeRow(
                    checked = loginUiState.rememberMe,
                    enabled = controlsEnabled,
                    onCheckedChange = viewModel::onRememberMeChange
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = animationStep >= 5,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { 25 },
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                )
            ) {
                LoginButtons(
                    viewModel = viewModel,
                    navController = navController,
                    enabled = controlsEnabled,
                    screenHeight = screenHeight
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = animationStep >= 6,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
                )
            ) {
                AppAuthor()
            }
        }


    }
}

@Composable
fun AppLogo(screenWidth: Float? = null, screenHeight: Float? = null) {
    val (w, h) = if (screenWidth != null && screenHeight != null) screenWidth to screenHeight else run {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        val size = windowInfo.containerSize
        size.width / density.density to size.height / density.density
    }
    // Responsive logo size
    val logoSize = when {
        h < 600 -> 180.dp  // Small screens
        h < 800 -> 220.dp  // Medium screens
        w > 720 -> 280.dp   // Large screens (tablets)
        else -> 250.dp                // Default
    }
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo BurgerCredit",
        modifier = Modifier.size(logoSize)
    )
}

@Composable
fun AppTitle(screenWidth: Float? = null, screenHeight: Float? = null) {
    val (w, h) = if (screenWidth != null && screenHeight != null) screenWidth to screenHeight else run {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        val size = windowInfo.containerSize
        size.width / density.density to size.height / density.density
    }
    // Responsive font size
    val fontSize = when {
        h < 600 -> 24.sp   // Small screens
        h < 800 -> 28.sp   // Medium screens
        w > 720 -> 36.sp    // Large screens (tablets)
        else -> 30.sp                 // Default
    }
    // Responsive letter spacing
    val letterSpacing = when {
        w < 320 -> 1.sp     // Very small screens
        w < 480 -> 1.5.sp   // Small screens
        else -> 2.sp                  // Default
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BURGER",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LoginColors.buttonText,
                fontSize = fontSize,
                letterSpacing = letterSpacing,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.15f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = "CREDIT",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LoginColors.logoBackground,
                fontSize = fontSize,
                letterSpacing = letterSpacing,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.15f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}

@Composable
fun UsernameInput(
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    enabled: Boolean = true,
    screenHeight: Float? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LoginTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Username",
            placeholder = "Username",
            trailingIcon = {
                LoginInputIcon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Username Icon"
                )
            },
            isPassword = false,
            hasError = error != null,
            enabled = enabled,
            screenHeight = screenHeight
        )
        if (error != null) {
            Text(
                text = error,
                color = LoginColors.link,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    enabled: Boolean = true,
    screenHeight: Float? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LoginTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Password",
            placeholder = "Password",
            trailingIcon = {
                LoginInputIcon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon"
                )
            },
            isPassword = true,
            hasError = error != null,
            enabled = enabled,
            screenHeight = screenHeight
        )
        if (error != null) {
            Text(
                text = error,
                color = LoginColors.link,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun RememberMeRow(checked: Boolean, enabled: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = LoginColors.buttonPrimary,
                uncheckedColor = LoginColors.inputIcon,
                checkmarkColor = LoginColors.buttonText,
                disabledCheckedColor = LoginColors.buttonPrimary, // Siempre naranja si está marcado
                disabledUncheckedColor = LoginColors.inputIcon,
                disabledIndeterminateColor = LoginColors.inputIcon
            )
        )
        Text(
            text = "Remember me",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) LoginColors.dark else if (checked) LoginColors.buttonPrimary else LoginColors.inputIcon,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun LoginInputIcon(
    imageVector: ImageVector,
    contentDescription: String,
    tint: Color = LoginColors.inputIcon
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint
    )
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    hasError: Boolean = false,
    enabled: Boolean = true,
    screenHeight: Float? = null
) {
    val h = screenHeight ?: run {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        windowInfo.containerSize.height / density.density
    }
    // Responsive field height
    val fieldHeight = when {
        h < 600 -> 48.dp  // Small screens
        h < 800 -> 52.dp  // Medium screens
        else -> 56.dp                // Large screens
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LoginColors.dark,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LoginColors.inputBackground,
                focusedContainerColor = LoginColors.inputBackground,
                disabledContainerColor = LoginColors.inputBackground,
                unfocusedBorderColor = if (hasError) LoginColors.link else Color.Transparent,
                focusedBorderColor = if (hasError) LoginColors.link else LoginColors.inputIcon,
                disabledBorderColor = Color.Transparent,
                cursorColor = LoginColors.dark,
                focusedTextColor = LoginColors.dark,
                unfocusedTextColor = LoginColors.dark,
                disabledTextColor = LoginColors.inputIcon,
                focusedPlaceholderColor = LoginColors.inputIcon,
                unfocusedPlaceholderColor = LoginColors.inputIcon,
                disabledPlaceholderColor = LoginColors.inputIcon
            ),
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}

@Composable
fun LoginActionButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    showProgress: Boolean = false,
    enabled: Boolean = true,
    screenHeight: Float? = null
) {
    val h = screenHeight ?: run {
        val windowInfo = LocalWindowInfo.current
        val density = LocalDensity.current
        windowInfo.containerSize.height / density.density
    }
    // Responsive button height
    val buttonHeight = when {
        h < 600 -> 44.dp  // Small screens
        h < 800 -> 46.dp  // Medium screens
        else -> 48.dp                // Large screens
    }
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        border = null,
        enabled = enabled && !isLoading
    ) {
        if (showProgress) {
            CircularProgressIndicator(
                color = textColor,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@Composable
fun LoginButtons(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController,
    enabled: Boolean = true,
    screenHeight: Float? = null
) {
    val loginUiState by viewModel.loginUiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LoginActionButton(
            text = if (loginUiState.result is LoginResultState.Loading && !loginUiState.rememberMe) "" else "Login",
            onClick = { viewModel.onLogin() },
            containerColor = LoginColors.buttonPrimary,
            textColor = LoginColors.buttonText,
            isLoading = loginUiState.result is LoginResultState.Loading && !loginUiState.rememberMe,
            showProgress = loginUiState.result is LoginResultState.Loading && !loginUiState.rememberMe,
            enabled = enabled,
            screenHeight = screenHeight
        )
        if (!loginUiState.adminExists) {
            LoginActionButton(
                text = "Sign Up",
                onClick = { navController.navigate(AppRoute.RegisterScreen.route) },
                containerColor = LoginColors.buttonSecondary,
                textColor = LoginColors.buttonText,
                isLoading = loginUiState.result is LoginResultState.Loading && !loginUiState.rememberMe,
                showProgress = false,
                enabled = enabled,
                screenHeight = screenHeight
            )
        }
    }
}

@Composable
fun AppAuthor() {
    Text(
        text = "Developed by JunaxerDev",
        style = MaterialTheme.typography.bodySmall.copy(
            color = LoginColors.inputIcon,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AutoLoginDialog(screenWidth: Float, screenHeight: Float) {
    val w = screenWidth
    val h = screenHeight
    // Responsive sizing
    val cardPadding = when {
        w < 320 -> 16.dp  // Very small screens
        w < 480 -> 20.dp  // Small screens
        w > 720 -> 32.dp  // Large screens (tablets)
        else -> 24.dp               // Default
    }
    val contentPadding = when {
        w < 320 -> 20.dp  // Very small screens
        w < 480 -> 24.dp  // Small screens
        w > 720 -> 40.dp  // Large screens (tablets)
        else -> 32.dp               // Default
    }
    val progressSize = when {
        h < 600 -> 40.dp  // Small screens
        h < 800 -> 48.dp  // Medium screens
        w > 720 -> 56.dp   // Large screens (tablets)
        else -> 48.dp                // Default
    }
    val strokeWidth = when {
        w > 720 -> 5.dp    // Large screens
        else -> 4.dp                 // Default
    }
    val fontSize = when {
        h < 600 -> 18.sp   // Small screens
        h < 800 -> 22.sp   // Medium screens
        w > 720 -> 26.sp    // Large screens (tablets)
        else -> 22.sp                 // Default
    }
    val spacing = when {
        h < 600 -> 16.dp   // Small screens
        h < 800 -> 20.dp   // Medium screens
        w > 720 -> 28.dp    // Large screens (tablets)
        else -> 24.dp                 // Default
    }
    // Fondo semitransparente para dar enfoque (sin blur)
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
    )
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
                .padding(cardPadding),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = LoginColors.background
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 24.dp
            ),
            border = BorderStroke(1.dp, LoginColors.buttonPrimary.copy(alpha = 0.12f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                // Fondo con gradiente para el loader
                Box(
                    modifier = Modifier
                        .size(progressSize + 16.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LoginColors.buttonPrimary.copy(alpha = 0.18f),
                                    Color.Transparent
                                ),
                                center = Offset((progressSize + 16.dp).toPx() / 2, (progressSize + 16.dp).toPx() / 2),
                                radius = (progressSize + 16.dp).toPx() / 2
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LoginColors.buttonPrimary,
                        modifier = Modifier.size(progressSize),
                        strokeWidth = strokeWidth
                    )
                }
                // Texto principal
                Text(
                    text = "Verificando credenciales",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = LoginColors.dark,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.12f),
                            offset = Offset(1f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
                // Subtítulo opcional
                Text(
                    text = "Por favor, espera un momento...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LoginColors.dark.copy(alpha = 0.7f),
                        fontSize = (fontSize.value * 0.68).sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Extensión para convertir Dp a px en Composable
@Composable
private fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

