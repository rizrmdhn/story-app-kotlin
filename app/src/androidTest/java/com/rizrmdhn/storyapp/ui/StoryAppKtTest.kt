package com.rizrmdhn.storyapp.ui

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.rizrmdhn.core.di.databaseModule
import com.rizrmdhn.core.di.networkModule
import com.rizrmdhn.core.di.preferencesModule
import com.rizrmdhn.core.di.repositoryModule
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.assertCurrentRouteName
import com.rizrmdhn.storyapp.di.useCaseModule
import com.rizrmdhn.storyapp.di.viewModelModule
import com.rizrmdhn.storyapp.onNodeWithStringId
import com.rizrmdhn.storyapp.ui.navigation.Screen
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StoryAppKtTest : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@StoryAppKtTest)
            modules(
                listOf(
                    databaseModule,
                    preferencesModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            StoryAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                StoryApp(
                    navController = navController,
                    viewModel = koinViewModel(),
                    context = LocalContext.current
                )
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Login.route)
    }


    @Test
    fun login_withInvalidEmail() {
        composeTestRule.onNodeWithStringId(R.string.email).performClick()
        composeTestRule.onNodeWithStringId(R.string.email).performTextInput("rizrmdhn")

    }
}