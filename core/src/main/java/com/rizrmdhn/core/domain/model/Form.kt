package com.rizrmdhn.core.domain.model

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier

data class Form(
    val title: String,
    val placeholder: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val modifier: Modifier = Modifier
)