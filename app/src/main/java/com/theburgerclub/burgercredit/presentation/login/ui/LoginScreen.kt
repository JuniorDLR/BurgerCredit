package com.theburgerclub.burgercredit.presentation.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.theburgerclub.burgercredit.presentation.theme.LoginColors
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.R
import com.theburgerclub.burgercredit.presentation.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
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
        LoginBody(loginViewModel)
    }
}

@Composable
fun LoginBody(viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var animationStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(6) { step ->
            delay(120)
            animationStep = step + 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.Top)
    ) {
        AnimatedVisibility(
            visible = animationStep >= 1,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -60 })
        ) { AppLogo() }
        AnimatedVisibility(
            visible = animationStep >= 2,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
        ) { AppTitle() }
        AnimatedVisibility(
            visible = animationStep >= 3,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmailInput(uiState.email, onValueChange = viewModel::onEmailChange)
                PasswordInput(uiState.password, onValueChange = viewModel::onPasswordChange)
            }
        }
        AnimatedVisibility(
            visible = animationStep >= 4,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
        ) {
            RememberMeRow(uiState.rememberMe, onCheckedChange = viewModel::onRememberMeChange)
        }
        AnimatedVisibility(
            visible = animationStep >= 5,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 60 })
        ) {
            LoginButtons()
        }
        AnimatedVisibility(
            visible = animationStep >= 6,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 80 })
        ) {
            AppAuthor()
        }
    }
}

@Composable
fun AppLogo() {
    Box(
        modifier = Modifier
            .size(170.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LoginColors.logoBackground,
                        LoginColors.logoBackground.copy(alpha = 0.7f),
                        LoginColors.background
                    ),
                    center = Offset(85f, 85f),
                    radius = 120f
                ),
                shape = CircleShape
            )
            .border(
                width = 4.dp,
                color = Color.White,
                shape = CircleShape
            )
            .shadow(12.dp, CircleShape, clip = false),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo BurgerCredit",
            modifier = Modifier.size(120.dp)
        )
    }
}

@Composable
fun AppTitle() {
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
                fontSize = 30.sp,
                letterSpacing = 2.sp,
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
                fontSize = 30.sp,
                letterSpacing = 2.sp,
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
fun EmailInput(value: String, onValueChange: (String) -> Unit) {
    LoginTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Email",
        placeholder = "Email",
        trailingIcon = {
            LoginInputIcon(
                imageVector = Icons.Default.Person,
                contentDescription = "Email Icon"
            )
        },
        isPassword = false
    )
}

@Composable
fun PasswordInput(value: String, onValueChange: (String) -> Unit) {
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
        isPassword = true
    )
}

@Composable
fun RememberMeRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = LoginColors.buttonPrimary,
                uncheckedColor = LoginColors.inputIcon,
                checkmarkColor = LoginColors.buttonText
            )
        )
        Text(
            text = "Remember me",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LoginColors.dark,
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
    isPassword: Boolean = false
) {
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
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LoginColors.inputBackground,
                focusedContainerColor = LoginColors.inputBackground,
                disabledContainerColor = LoginColors.inputBackground,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = LoginColors.inputIcon,
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
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        border = null
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
    }
}

@Composable
fun LoginButtons() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LoginActionButton(
            text = "Login",
            onClick = {  },
            containerColor = LoginColors.buttonPrimary,
            textColor = LoginColors.buttonText
        )
        LoginActionButton(
            text = "Sign Up",
            onClick = {  },
            containerColor = LoginColors.buttonSecondary,
            textColor = LoginColors.buttonText
        )
    }
}

@Composable
fun AppAuthor() {
    Text(
        text = "Developed by JuanxerDev",
        style = MaterialTheme.typography.bodySmall.copy(
            color = LoginColors.inputIcon,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}