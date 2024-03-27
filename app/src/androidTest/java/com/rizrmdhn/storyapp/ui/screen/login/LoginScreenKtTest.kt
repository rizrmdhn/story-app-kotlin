package com.rizrmdhn.storyapp.ui.screen.login

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.assertCurrentRouteName
import com.rizrmdhn.storyapp.onNodeWithStringId
import com.rizrmdhn.storyapp.ui.StoryApp
import com.rizrmdhn.storyapp.ui.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController
    private val email = mutableStateOf("")
    @Before
    fun setUp() {
        composeTestRule.setContent {
            StoryAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                LoginScreen(
                    navigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    email = email.value,
                    isEmailValid = true,
                    emailMessage = "",
                    password = "",
                    isPasswordValid = true,
                    passwordMessage = "",
                    initialEmail = false,
                    initialPassword = false,
                    onChangeEmail = {},
                    onChangePassword = {},
                    onLogin = { email, password ->

                    },
                    isLoading = false
                )
            }
        }
        composeTestRule.onRoot().printToLog("LoginScreenKtTest")
    }

    @Test
    fun loginScreen_isDisplayed() {
        composeTestRule.onNodeWithStringId(
            R.string.email
        ).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(
            R.string.password
        ).assertIsDisplayed()
    }

    @Test
    fun loginScreen_typeEmail_withInvalidEmail() {
        composeTestRule.onNodeWithStringId(
            R.string.email
        ).performTextInput("rizrmdhn")
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(
                R.string.error_icon
            )
        ).assertIsDisplayed()
    }
}