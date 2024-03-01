package com.rizrmdhn.core.domain.model

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

data class Form(
    val title: String,
    val placeholder: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val visualTransformation: VisualTransformation,
    val leadingIcon: @Composable (() -> Unit)? = null,
    val trailingIcon: @Composable (() -> Unit)? = null,
    val keyboardOptions: KeyboardOptions,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val modifier: Modifier = Modifier
)