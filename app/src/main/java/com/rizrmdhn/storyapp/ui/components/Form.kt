package com.rizrmdhn.storyapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizrmdhn.core.domain.model.Form
import com.rizrmdhn.core.ui.theme.StoryAppTheme

@Composable
fun FormComp(
    formData: List<Form>,
    buttonText: String = "Login",
    onClickButton: () -> Unit,
    isLoading: Boolean = false
) {
    formData.forEach { data ->
        OutlinedTextField(
            label = {
                Text(
                    text = data.title
                )
            },
            placeholder = {
                Text(
                    text = data.placeholder
                )
            },
            value = data.value,
            onValueChange = data.onValueChange,
            singleLine = true,
            keyboardOptions = data.keyboardOptions,
            isError = data.isError,
            supportingText = {
                if (data.isError) {
                    Text(
                        text = data.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = data.visualTransformation,
            leadingIcon = data.leadingIcon,
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    data.trailingIcon?.invoke()
                    if (data.isError) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Error icon",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            modifier = Modifier
                .width(280.dp)
        )
        Spacer(
            modifier = Modifier.height(26.dp)
        )
    }
    TextButton(
        enabled = !isLoading,
        onClick = {
            onClickButton()
        },
        modifier = Modifier
            .width(280.dp)
            .clip(
                RoundedCornerShape(
                    10.dp
                )
            )
            .background(
                if (isLoading) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
            )
        } else {
            Text(
                text = buttonText,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLoginPreview() {
    StoryAppTheme {
        FormComp(
            formData = listOf(
                Form(
                    title = "Username",
                    placeholder = "Enter your username",
                    value = "",
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions.Default,
                    visualTransformation = VisualTransformation.None
                ),
            ),
            buttonText = "Login",
            onClickButton = {},
            isLoading = true
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultDarkLoginPreview() {
    StoryAppTheme {
        FormComp(
            formData = listOf(
                Form(
                    title = "Username",
                    placeholder = "Enter your username",
                    value = "",
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions.Default,
                    visualTransformation = VisualTransformation.None
                )
            ),
            buttonText = "Login",
            onClickButton = {}
        )
    }
}