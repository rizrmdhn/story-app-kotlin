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
fun RegisterScreen(
    navController: NavHostController,
)  {
    RegisterContent(
        navigateToLogin = {
            navController.navigate(
                Screen.Login.route
            )
        }
    )
}

@Composable
fun RegisterContent(
    navigateToLogin: () -> Unit,
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
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "Name must be at least 3 characters",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            ),
            Form(
                title = "Email",
                placeholder = "Enter your email",
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "Invalid email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ),
            Form(
                title = "Password",
                placeholder = "Enter your password",
                value = "",
                onValueChange = {},
                isError = false,
                errorMessage = "Password must be at least 8 characters",
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
            },
            isLoading = false
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
            )
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
            )
        )
    }
}