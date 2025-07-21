package com.theburgerclub.burgercredit.presentation.shared.model

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.vector.ImageVector


data class FormFieldData(
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String,
    val placeholder: String,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val leadingIcon: ImageVector? = null,
    val onClear: (() -> Unit)? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) 