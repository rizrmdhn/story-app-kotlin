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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rizrmdhn.core.common.Constants
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.navigation.Screen
import com.rizrmdhn.storyapp.ui.screen.about.AboutScreen
import com.rizrmdhn.storyapp.ui.screen.add.AddScreen
import com.rizrmdhn.storyapp.ui.screen.detail.DetailScreen
import com.rizrmdhn.storyapp.ui.screen.home.HomeScreen
import com.rizrmdhn.storyapp.ui.screen.loading.LoadingScreen
import com.rizrmdhn.storyapp.ui.screen.login.LoginScreen
import com.rizrmdhn.storyapp.ui.screen.register.RegisterScreen
import com.rizrmdhn.storyapp.ui.screen.settings.SettingScreen
import org.koin.androidx.compose.koinViewModel

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

    // login and register
    val name by viewModel.name.collectAsState()
    val isNameValid by viewModel.isNameValid.collectAsState()
    val nameMessage by viewModel.nameMessage.collectAsState()


    val email by viewModel.email.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val emailMessage by viewModel.emailMessage.collectAsState()

    val password by viewModel.password.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val passwordMessage by viewModel.passwordMessage.collectAsState()

    val initialName by viewModel.initialName.collectAsState()
    val initialEmail by viewModel.initialEmail.collectAsState()
    val initialPassword by viewModel.initialPassword.collectAsState()


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
                                    navigateToRegister = {
                                        viewModel.setEmailToEmpty()
                                        viewModel.setPasswordToEmpty()
                                        viewModel.setNameToEmpty()
                                        navController.navigate(Screen.Register.route)
                                    },
                                    onLogin = { email, password ->
                                        viewModel.login(email, password, context)
                                    },
                                    isLoading = loginIsLoading,
                                    initialEmail = initialEmail,
                                    initialPassword = initialPassword,
                                    onChangeEmail = { email ->
                                        viewModel.setEmail(email)
                                    },
                                    onChangePassword = { password ->
                                        viewModel.setPassword(password)
                                    },
                                    email = email,
                                    isEmailValid = isEmailValid,
                                    emailMessage = emailMessage,
                                    isPasswordValid = isPasswordValid,
                                    password = password,
                                    passwordMessage = passwordMessage,
                                )
                            }
                            composable(
                                Screen.Register.route
                            ) {
                                RegisterScreen(
                                    navigateToLogin = {
                                        viewModel.setEmailToEmpty()
                                        viewModel.setPasswordToEmpty()
                                        viewModel.setNameToEmpty()
                                        navController.navigate(Screen.Login.route)
                                    },
                                    onRegister = { name, email, password ->
                                        viewModel.register(
                                            name,
                                            email,
                                            password,
                                            context,
                                            navController
                                        )
                                    },
                                    isLoading = registerIsLoading,
                                    initialName = initialName,
                                    initialEmail = initialEmail,
                                    initialPassword = initialPassword,
                                    onChangeName = { name ->
                                        viewModel.setName(name)
                                    },
                                    onChangeEmail = { email ->
                                        viewModel.setEmail(email)
                                    },
                                    onChangePassword = { password ->
                                        viewModel.setPassword(password)
                                    },
                                    name = name,
                                    isNameValid = isNameValid,
                                    nameMessage = nameMessage,
                                    isEmailValid = isEmailValid,
                                    email = email,
                                    emailMessage = emailMessage,
                                    isPasswordValid = isPasswordValid,
                                    password = password,
                                    passwordMessage = passwordMessage,
                                )
                            }
                        } else {
                            composable(
                                Screen.Home.route
                            ) {
                                HomeScreen(
                                    navController = navController,
                                )
                            }
                            composable(
                                Screen.AddStory.route
                            ) {
                                AddScreen(
                                    navController = navController,
                                )
                            }
                            composable(
                                Screen.About.route
                            ) {
                                AboutScreen(
                                    navigateBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable(
                                Screen.Settings.route
                            ) {
                                SettingScreen(
                                    darkMode = darkMode,
                                    setDarkMode = { newDarkMode ->
                                        viewModel.setDarkMode(newDarkMode)
                                    },
                                    locale = locale,
                                    setLocale = { newLocale ->
                                        viewModel.setLocaleSetting(newLocale, context)
                                    },
                                    onLogout = {
                                        viewModel.logout(context)
                                    },
                                    navigateBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable(
                                route = Screen.DetailStory.route,
                                arguments = listOf(navArgument(Constants.ID) {
                                    type = NavType.StringType
                                })
                            ) {
                                val id = it.arguments?.getString(Constants.ID) ?: ""
                                DetailScreen(
                                    storyId = id,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
