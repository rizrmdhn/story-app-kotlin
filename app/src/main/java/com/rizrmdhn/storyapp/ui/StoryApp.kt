package com.rizrmdhn.storyapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.ui.navigation.Screen
import com.rizrmdhn.storyapp.ui.screen.login.LoginScreen
import com.rizrmdhn.storyapp.ui.screen.register.RegisterScreen

@Composable
fun StoryApp(
    navController: NavHostController = rememberNavController()
) {
    StoryAppTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(
                    Screen.Login.route
                ) {
                    LoginScreen(
                        navController = navController
                    )
                }
                composable(
                    Screen.Register.route
                ) {
                    RegisterScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}