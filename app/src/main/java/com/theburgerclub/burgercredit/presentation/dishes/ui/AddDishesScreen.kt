package com.theburgerclub.burgercredit.presentation.dishes.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.theburgerclub.burgercredit.presentation.shared.GenericFormScreen
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.theburgerclub.burgercredit.presentation.dishes.viewmodel.DishViewModel
import kotlinx.coroutines.launch
import com.theburgerclub.burgercredit.presentation.shared.TopAppBarShared
import com.theburgerclub.burgercredit.presentation.shared.model.FormFieldData
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.LaunchedEffect
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import com.theburgerclub.burgercredit.presentation.dishes.model.ImageError
import com.theburgerclub.burgercredit.presentation.dishes.model.StepImageState
import com.theburgerclub.burgercredit.presentation.shared.model.rememberFormResponsiveConfig


@Composable
fun AddDishesScreen(
    navController: NavHostController,
    dishId: Long? = null,
    viewModel: DishViewModel = hiltViewModel()
) {
    val uiState by viewModel.dishUiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Precargar datos si es edición
    LaunchedEffect(dishId) {
        if (dishId != null && uiState.selectedDish?.id != dishId) {
            viewModel.loadDishById(dishId)
        }
    }

    val isEdit = uiState.selectedDish != null && dishId != null
    val topBarTitle = if (isEdit) "Edit Dish" else "Add Dish"

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.uploadImage(uri, context)
        }
    )

    val responsiveConfig = rememberFormResponsiveConfig()

    Scaffold(
        topBar = {
            TopAppBarShared(
                nameTopBar = topBarTitle,
                showNavigationIcon = true,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->

        GenericFormScreen(
            title = topBarTitle,
            subtitle = if (isEdit) "Edit dish details" else "Register a new dish",
            description = if (isEdit) "Update the information below and save your changes." else "Fill in the details below to add a new dish.",
            fields = listOf(
                FormFieldData(
                    value = uiState.dishNameInput,
                    onValueChange = {
                        viewModel.onDishNameInputChange(it)
                        if (uiState.dishNameError != null) viewModel.clearErrors()
                    },
                    label = "Dish Name",
                    placeholder = "Enter dish name",
                    isError = uiState.dishNameError != null,
                    errorMessage = uiState.dishNameError,
                    leadingIcon = Icons.Default.Fastfood,
                    onClear = { viewModel.onDishNameInputChange("") }
                ),
                FormFieldData(
                    value = uiState.priceInput,
                    onValueChange = {
                        viewModel.onPriceInputChange(it)
                        if (uiState.priceError != null) viewModel.clearErrors()
                    },
                    label = "Price",
                    placeholder = "Enter price",
                    isError = uiState.priceError != null,
                    errorMessage = uiState.priceError,
                    leadingIcon = null,
                    onClear = { viewModel.onPriceInputChange("") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            ),
            onSubmit = {
                scope.launch {
                    val success = if (isEdit) viewModel.validateAndUpdateDish() else viewModel.validateAndAddDish()
                    if (success) {
                        Toast.makeText(
                            context,
                            if (isEdit) "Dish updated!" else "Dish saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                    }
                }
            },
            submitButtonText = if (isEdit) "Save Changes" else "Save Dish",
            isLoading = uiState.isLoading,
            enabled = uiState.stepImageState != StepImageState.LOADING,
            topContent = {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = "Dish image",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(responsiveConfig.horizontalPadding),
            cardPadding = PaddingValues(
                top = responsiveConfig.verticalPadding,
                start = responsiveConfig.horizontalPadding,
                end = responsiveConfig.horizontalPadding,
                bottom = responsiveConfig.verticalPadding
            ),
            extraContent = {
                ImagePickerCard(
                    imageUri = uiState.imageUri,
                    imageState = uiState.stepImageState,
                    imageError = uiState.imageError,
                    onPickImage = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    onRemoveImage = {
                        viewModel.setImageUri(null)
                        viewModel.setImageError(ImageError.None)
                        viewModel.changeUploadImageState(StepImageState.NONE)
                    }
                )
            }
        )

    }
}

@Composable
fun ImagePickerCard(
    imageUri: Uri?,
    imageState: StepImageState,
    imageError: ImageError,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPickImage() }
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (imageState) {
                StepImageState.NONE, StepImageState.CLOSE -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tap to select an image",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "PNG, JPG, JPEG, WEBP (max. 2MB)",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )

                    }
                }

                StepImageState.LOADING -> {
                    CircularProgressIndicator()
                }

                StepImageState.IMAGE -> {
                    Box(Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(90.dp),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = onRemoveImage,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Eliminar imagen")
                        }
                    }
                }
            }
            if (imageError != ImageError.None && imageError != ImageError.Empty) {
                Text(
                    text = when (imageError) {
                        ImageError.ErrorSize -> "Image is too large"
                        ImageError.ErrorExtension -> "Unsupported format"
                        else -> "Image error"
                    },
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                )
            }
        }
    }
}