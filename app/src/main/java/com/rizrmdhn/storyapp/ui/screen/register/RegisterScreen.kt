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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rizrmdhn.core.domain.model.Form
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.components.FormComp
import com.rizrmdhn.storyapp.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterScreenViewModel = koinViewModel(),
    onRegister: (String, String, String) -> Unit,
    isLoading: Boolean
) {
    val name by viewModel.name.collectAsState()
    val isNameValid by viewModel.isNameValid.collectAsState()
    val nameMessage by viewModel.nameMessage.collectAsState()

    val email by viewModel.email.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val emailMessage by viewModel.emailMessage.collectAsState()

    val showPassword by viewModel.showPassword.collectAsState()

    val password by viewModel.password.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val passwordMessage by viewModel.passwordMessage.collectAsState()

    val initialName by viewModel.initialName.collectAsState()
    val initialEmail by viewModel.initialEmail.collectAsState()
    val initialPassword by viewModel.initialPassword.collectAsState()

    RegisterContent(
        navigateToLogin = {
            navController.navigate(
                Screen.Login.route
            )
        },
        name = name,
        onChangeName = {
            viewModel.setName(it)
        },
        isNameValid = isNameValid,
        initialName = initialName,
        nameMessage = nameMessage,
        email = email,
        onChangeEmail = {
            viewModel.setEmail(it)
        },
        isEmailValid = isEmailValid,
        initialEmail = initialEmail,
        emailMessage = emailMessage,
        password = password,
        onChangePassword = {
            viewModel.setPassword(it)
        },
        isPasswordValid = isPasswordValid,
        initialPassword = initialPassword,
        passwordMessage = passwordMessage,
        showPassword = showPassword,
        setShowPassword = {
            viewModel.setShowPassword(!showPassword)
        },
        onRegister = {
            if (isNameValid && isEmailValid && isPasswordValid) {
                onRegister(name, email, password)
            } else if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                viewModel.setNameMessage("Name is required")
                viewModel.setEmailMessage("Email is required")
                viewModel.setPasswordMessage("Password is required")
                viewModel.setInitialName(false)
                viewModel.setInitialEmail(false)
                viewModel.setInitialPassword(false)
            } else if (name.isEmpty()) {
                viewModel.setNameMessage("Name is required")
                viewModel.setInitialName(false)
            } else if (email.isEmpty()) {
                viewModel.setEmailMessage("Email is required")
                viewModel.setInitialEmail(false)
            } else if (password.isEmpty()) {
                viewModel.setPasswordMessage("Password is required")
                viewModel.setInitialPassword(false)
            }
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
            navController = NavHostController(
                LocalContext.current
            ),
            onRegister = { _, _, _ -> },
            isLoading = false
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultDarkLoginPreview() {
    StoryAppTheme {
        RegisterScreen(
            navController = NavHostController(
                LocalContext.current
            ),
            onRegister = { _, _, _ -> },
            isLoading = false
        )
    }
}