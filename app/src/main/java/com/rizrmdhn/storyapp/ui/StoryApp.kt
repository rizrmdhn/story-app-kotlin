package com.rizrmdhn.storyapp.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.navigation.Screen
import com.rizrmdhn.storyapp.ui.screen.home.HomeScreen
import com.rizrmdhn.storyapp.ui.screen.loading.LoadingScreen
import com.rizrmdhn.storyapp.ui.screen.login.LoginScreen
import com.rizrmdhn.storyapp.ui.screen.register.RegisterScreen
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun StoryApp(
    navController: NavHostController = rememberNavController(),
    viewModel: StoryAppViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val initialRender by viewModel.initialRender.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val token by viewModel.token.collectAsState()
    val loginIsLoading by viewModel.loginIsLoading.collectAsState()
    val registerIsLoading by viewModel.registerIsLoading.collectAsState()
    val locale by viewModel.locale.collectAsState()

    viewModel.getAccessToken()
    viewModel.getDarkMode()
    viewModel.getLocaleSetting(context)
    StoryAppTheme(
        darkTheme = darkMode
    ) {
        Scaffold(
            floatingActionButton = {
                if (token.isBlank()) {
                    IconButton(
                        onClick = {
                            viewModel.setDarkMode(!darkMode)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(
                                CircleShape
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (darkMode) {
                                    R.drawable.baseline_sunny_24
                                } else {
                                    R.drawable.baseline_dark_mode_24
                                }
                            ),
                            contentDescription = "DarkMode",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (initialRender) {
                    Screen.Loading.route
                } else if (token.isEmpty()) {
                    Screen.Login.route
                } else {
                    Screen.Home.route
                },
                modifier = Modifier.padding(innerPadding),
            ) {
                when (initialRender) {
                    true -> {
                        composable(
                            Screen.Loading.route
                        ) {
                            LoadingScreen()
                        }
                    }

                    false -> {
                        if (token.isEmpty()) {
                            composable(
                                Screen.Login.route
                            ) {
                                LoginScreen(
                                    navController = navController,
                                    onLogin = { email, password ->
                                        viewModel.login(email, password, context)
                                    },
                                    isLoading = loginIsLoading
                                )
                            }
                            composable(
                                Screen.Register.route
                            ) {
                                RegisterScreen(
                                    navController = navController,
                                    onRegister = { name, email, password ->
                                        viewModel.register(
                                            name,
                                            email,
                                            password,
                                            context,
                                            navController
                                        )
                                    },
                                    isLoading = registerIsLoading
                                )
                            }
                        } else {
                            composable(
                                Screen.Home.route
                            ) {
                                HomeScreen(
                                    navController = navController,
                                    locale = locale,
                                    setLocale = { newLocale ->
                                        viewModel.setLocaleSetting(newLocale, context)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
