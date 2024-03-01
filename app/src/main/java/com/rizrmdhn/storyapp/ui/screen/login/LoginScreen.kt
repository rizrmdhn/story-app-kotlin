package com.rizrmdhn.storyapp.ui.screen.login

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rizrmdhn.core.domain.model.Form
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.ui.components.FormComp
import com.rizrmdhn.storyapp.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
) {
    LoginContent(
        navigateToRegister = {
            navController.navigate(
                Screen.Register.route
            )
        }
    )
}

@Composable
fun LoginContent(
    navigateToRegister: () -> Unit,
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
                title = "Email",
                placeholder = "Enter your email",
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "Email must be valid",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ),
            Form(
                title = "Password",
                placeholder = "Enter your password",
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "Password must be at least 6 characters",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        )

        Text(
            text = "Login",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(
            modifier = Modifier.height(26.dp)
        )
        FormComp(
            formData = formList,
            buttonText = "Login",
            onClickButton = {},
            isLoading = false
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row {
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Text(
                text = "Register",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navigateToRegister()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLoginPreview() {
    StoryAppTheme {
        LoginScreen(
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultDarkLoginPreview() {
    StoryAppTheme {
        LoginScreen(
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}
