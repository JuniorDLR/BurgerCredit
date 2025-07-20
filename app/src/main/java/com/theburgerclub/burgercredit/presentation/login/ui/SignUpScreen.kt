package com.theburgerclub.burgercredit.presentation.login.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
            SignUpTopAppBar(onBack = {
                if (viewModel.isNotLoading()) {
                    navController.popBackStack()
                }
            })
        }
    ) { paddingValues ->
        SignUpContent(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            signUpState = signUpState,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            viewModel = viewModel)

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTopAppBar(onBack: () -> Unit) {
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
        navigationIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LoginColors.logoBackground,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = LoginColors.logoBackground,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍔",
                    fontSize = 40.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BurgerCredit Admin",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LoginColors.logoBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create admin account to manage the system",
                fontSize = 14.sp,
                color = LoginColors.inputIcon,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        SignUpTextField(
            value = signUpState.username,
            onValueChange = onUsernameChange,
            label = "Username",
            placeholder = "Username",
            leadingIcon = Icons.Default.Person,
            error = signUpState.usernameError
        )

        // Password Field
        SignUpTextField(
            value = signUpState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            error = signUpState.passwordError
        )

        // Confirm Password Field
        SignUpTextField(
            value = signUpState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm Password",
            placeholder = "Confirm Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            error = signUpState.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button
        SignUpButton(
            text = if (signUpState.result is SignUpResultState.Loading) "" else "Sign Up",
            onClick = viewModel::onSignUp,
            containerColor = LoginColors.buttonPrimary,
            textColor = LoginColors.buttonText,
            isLoading = signUpState.result is SignUpResultState.Loading
        )

        Spacer(modifier = Modifier.weight(1f))

        // Login Link
        SignUpLoginLink(viewModel=viewModel,navController = navController)
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
    error: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
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


