package com.rizrmdhn.storyapp.ui.screen.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizrmdhn.core.domain.model.Form
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.components.FormComp
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    viewModel: RegisterScreenViewModel = koinViewModel(),
    name: String,
    onChangeName: (String) -> Unit,
    nameMessage: String,
    isNameValid: Boolean,
    initialName: Boolean,
    email: String,
    onChangeEmail: (String) -> Unit,
    isEmailValid: Boolean,
    initialEmail: Boolean,
    emailMessage: String,
    password: String,
    onChangePassword: (String) -> Unit,
    isPasswordValid: Boolean,
    initialPassword: Boolean,
    passwordMessage: String,
    onRegister: (String, String, String) -> Unit,
    isLoading: Boolean
) {
    val showPassword by viewModel.showPassword.collectAsState()

    RegisterContent(
        navigateToLogin = navigateToLogin,
        name = name,
        onChangeName = {
            onChangeName(it)
        },
        isNameValid = isNameValid,
        initialName = initialName,
        nameMessage = nameMessage,
        email = email,
        onChangeEmail = {
            onChangeEmail(it)
        },
        isEmailValid = isEmailValid,
        initialEmail = initialEmail,
        emailMessage = emailMessage,
        password = password,
        onChangePassword = {
            onChangePassword(it)
        },
        isPasswordValid = isPasswordValid,
        initialPassword = initialPassword,
        passwordMessage = passwordMessage,
        showPassword = showPassword,
        setShowPassword = {
            viewModel.setShowPassword(!showPassword)
        },
        onRegister = {
            onRegister(name, email, password)
        },
        isLoading = isLoading
    )
}

@Composable
fun RegisterContent(
    navigateToLogin: () -> Unit,
    name: String,
    onChangeName: (String) -> Unit,
    isNameValid: Boolean,
    initialName: Boolean,
    nameMessage: String,
    email: String,
    onChangeEmail: (String) -> Unit,
    isEmailValid: Boolean,
    initialEmail: Boolean,
    emailMessage: String,
    password: String,
    onChangePassword: (String) -> Unit,
    isPasswordValid: Boolean,
    initialPassword: Boolean,
    passwordMessage: String,
    showPassword: Boolean,
    setShowPassword: () -> Unit,
    onRegister: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        val formList = listOf(
            Form(
                title = "Name",
                placeholder = "Enter your name",
                value = name,
                onValueChange = {
                    onChangeName(it)
                },
                isError = if (initialName) false else !isNameValid,
                errorMessage = nameMessage,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            ),
            Form(
                title = "Email",
                placeholder = "Enter your email",
                value = email,
                onValueChange = {
                    onChangeEmail(it)
                },
                isError = if (initialEmail) false else !isEmailValid,
                errorMessage = emailMessage,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ),
            Form(
                title = "Password",
                placeholder = "Enter your password",
                value = password,
                onValueChange = {
                    onChangePassword(it)
                },
                trailingIcon = {
                    IconButton(onClick = setShowPassword) {
                        Icon(
                            painter = painterResource(
                                if (showPassword) {
                                    R.drawable.baseline_visibility_24
                                } else {
                                    R.drawable.baseline_visibility_off_24
                                }
                            ),
                            contentDescription = "Show password",
                        )
                    }
                },
                isError = if (initialPassword) false else !isPasswordValid,
                errorMessage = passwordMessage,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        )

        Text(
            text = "Register",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(
            modifier = Modifier.height(26.dp)
        )
        FormComp(
            formData = formList,
            buttonText = "Register",
            onClickButton = {
                if (!isLoading) {
                    onRegister()
                }
            },
            isLoading = isLoading
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navigateToLogin()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLoginPreview() {
    StoryAppTheme {
        RegisterScreen(
            navigateToLogin = {},
            onRegister = { _, _, _ -> },
            isLoading = false,
            email = "",
            isEmailValid = true,
            emailMessage = "",
            initialEmail = true,
            password = "",
            isPasswordValid = true,
            passwordMessage = "",
            initialPassword = true,
            onChangeEmail = {},
            onChangePassword = {},
            name = "",
            onChangeName = { },
            nameMessage = "",
            isNameValid = true,
            initialName = true
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultDarkLoginPreview() {
    StoryAppTheme {
        RegisterScreen(
            navigateToLogin = {},
            onRegister = { _, _, _ -> },
            isLoading = false,
            email = "",
            isEmailValid = true,
            emailMessage = "",
            initialEmail = true,
            password = "",
            isPasswordValid = true,
            passwordMessage = "",
            initialPassword = true,
            onChangeEmail = {},
            onChangePassword = {},
            name = "",
            onChangeName = { },
            nameMessage = "",
            isNameValid = true,
            initialName = true
        )
    }
}