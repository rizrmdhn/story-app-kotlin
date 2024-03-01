package com.rizrmdhn.storyapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.ui.navigation.Screen
import com.rizrmdhn.storyapp.ui.screen.home.HomeScreen
import com.rizrmdhn.storyapp.ui.screen.login.LoginScreen
import com.rizrmdhn.storyapp.ui.screen.register.RegisterScreen

@Composable
fun StoryApp(
    navController: NavHostController = rememberNavController()
) {
    var token by remember {
        mutableStateOf("")
    }

    StoryAppTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (token.isEmpty()) Screen.Login.route else Screen.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                if (token.isEmpty()) {
                    composable(
                        Screen.Login.route
                    ) {
                        LoginScreen(
                            navController = navController,
                            onLogin = {
                                token = "1234567890"
                            }
                        )
                    }
                    composable(
                        Screen.Register.route
                    ) {
                        RegisterScreen(
                            navController = navController
                        )
                    }
                } else {
                    composable(
                        Screen.Home.route
                    ) {
                        HomeScreen(
                            token = token,
                            removeToken = {
                                token = ""
                            }
                        )
                    }
                }
            }
        }
    }
}