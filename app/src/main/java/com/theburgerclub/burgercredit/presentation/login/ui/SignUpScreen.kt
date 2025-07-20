package com.theburgerclub.burgercredit.presentation.login.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.ui.text.style.TextAlign
import com.theburgerclub.burgercredit.presentation.login.model.SignUpUiState
import com.theburgerclub.burgercredit.presentation.login.model.SignUpResultState
import com.theburgerclub.burgercredit.presentation.login.viewmodel.SignUpViewModel
import com.theburgerclub.burgercredit.presentation.routes.AppRoute
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi


@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val signUpState by viewModel.signUpUiState.collectAsState()
    val context = LocalContext.current

    // Navigate to login on success
    LaunchedEffect(signUpState.result) {
        if (signUpState.result is SignUpResultState.Success) {
            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate(AppRoute.LoginScreen.route) {
                popUpTo(AppRoute.RegisterScreen.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            SignUpTopAppBar()
        }
    ) { paddingValues ->
        SignUpContent(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            signUpState = signUpState,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            viewModel = viewModel
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LoginColors.logoBackground,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    signUpState: SignUpUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    viewModel: SignUpViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    // Responsive spacing based on screen size
    val spacing = when {
        screenHeight < 600 -> 12.dp  // Small screens
        screenHeight < 800 -> 16.dp  // Medium screens
        else -> 20.dp                // Large screens
    }

    // Responsive horizontal padding
    val horizontalPadding = when {
        screenWidth < 320 -> 16.dp   // Very small screens
        screenWidth < 480 -> 20.dp   // Small screens
        screenWidth < 720 -> 24.dp   // Medium screens
        else -> 32.dp                // Large screens
    }

    // Check if device is in landscape mode
    val isLandscape = screenWidth > screenHeight

    val imeInsets = WindowInsets.ime.asPaddingValues()
    val keyboardHeight = imeInsets.calculateBottomPadding()

    val controlsEnabled = signUpState.result !is SignUpResultState.Loading

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = horizontalPadding, vertical = 10.dp)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing, Alignment.Top),
        contentPadding = PaddingValues(
            bottom = if (isLandscape) {
                if (keyboardHeight > 0.dp) 300.dp else 200.dp
            } else {
                if (keyboardHeight > 0.dp) 150.dp else 100.dp
            }
        )
    ) {
        item {
            SignUpWelcomeHeader()
        }


        item {
            // Username Field
            SignUpTextField(
                value = signUpState.username,
                onValueChange = onUsernameChange,
                label = "Username",
                placeholder = "Username",
                leadingIcon = Icons.Default.Person,
                error = signUpState.usernameError,
                enabled = controlsEnabled
            )
        }

        item {
            // Password Field
            SignUpTextField(
                value = signUpState.password,
                onValueChange = onPasswordChange,
                label = "Password",
                placeholder = "Password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                error = signUpState.passwordError,
                enabled = controlsEnabled
            )
        }

        item {
            // Confirm Password Field
            SignUpTextField(
                value = signUpState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirm Password",
                placeholder = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                error = signUpState.confirmPasswordError,
                enabled = controlsEnabled
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Sign Up Button
            SignUpButton(
                text = if (signUpState.result is SignUpResultState.Loading) "" else "Sign Up",
                onClick = viewModel::onSignUp,
                containerColor = LoginColors.buttonPrimary,
                textColor = LoginColors.buttonText,
                isLoading = signUpState.result is SignUpResultState.Loading
            )
        }


        item {
            // Login Link
            SignUpLoginLink(viewModel = viewModel, navController = navController)
        }

        item {
            Spacer(
                modifier = Modifier.height(
                    if (isLandscape) {
                        if (keyboardHeight > 0.dp) 250.dp else 150.dp
                    } else {
                        if (keyboardHeight > 0.dp) 100.dp else 20.dp
                    }
                )
            )
        }

    }
}

@Composable
fun SignUpWelcomeHeader() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = screenWidth > screenHeight
    val imeInsets = WindowInsets.ime.asPaddingValues()
    val keyboardHeight = imeInsets.calculateBottomPadding()
    val isKeyboardVisible = keyboardHeight > 0.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Logo Container - smaller in landscape and when keyboard is visible
        Box(
            modifier = Modifier
                .size(
                    when {
                        isLandscape && isKeyboardVisible -> 40.dp
                        isLandscape -> 60.dp
                        isKeyboardVisible -> 60.dp
                        else -> 80.dp
                    }
                )
                .background(
                    color = LoginColors.logoBackground,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🍔",
                fontSize = when {
                    isLandscape && isKeyboardVisible -> 20.sp
                    isLandscape -> 30.sp
                    isKeyboardVisible -> 30.sp
                    else -> 40.sp
                },
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(
                when {
                    isLandscape && isKeyboardVisible -> 4.dp
                    isLandscape -> 8.dp
                    isKeyboardVisible -> 8.dp
                    else -> 16.dp
                }
            )
        )

        Text(
            text = "BurgerCredit Admin",
            fontSize = when {
                isLandscape && isKeyboardVisible -> 16.sp
                isLandscape -> 20.sp
                isKeyboardVisible -> 20.sp
                else -> 24.sp
            },
            fontWeight = FontWeight.Bold,
            color = LoginColors.logoBackground,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(
                when {
                    isLandscape && isKeyboardVisible -> 2.dp
                    isLandscape -> 4.dp
                    isKeyboardVisible -> 4.dp
                    else -> 8.dp
                }
            )
        )

        Text(
            text = "Create admin account to manage the system",
            fontSize = when {
                isLandscape && isKeyboardVisible -> 10.sp
                isLandscape -> 12.sp
                isKeyboardVisible -> 12.sp
                else -> 14.sp
            },
            color = LoginColors.inputIcon,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    error: String? = null,
    enabled: Boolean = true
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LoginColors.inputBackground,
                focusedContainerColor = LoginColors.inputBackground,
                disabledContainerColor = LoginColors.inputBackground,
                unfocusedBorderColor = if (error != null) LoginColors.link else Color.Transparent,
                focusedBorderColor = if (error != null) LoginColors.link else LoginColors.inputIcon,
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
            leadingIcon = {
                LoginInputIcon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = LoginColors.inputIcon
                )
            },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            isError = error != null
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
fun SignUpButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        border = null,
        enabled = !isLoading
    ) {
        if (isLoading) {
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
fun SignUpLoginLink(navController: NavController, viewModel: SignUpViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account? ",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LoginColors.inputIcon,
                fontWeight = FontWeight.Medium
            )
        )

        TextButton(
            onClick = {
                if (viewModel.isNotLoading()) {
                    navController.navigate(AppRoute.LoginScreen.route)
                }
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = LoginColors.buttonPrimary
            ),
            modifier = Modifier.padding(0.dp)
        ) {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}


