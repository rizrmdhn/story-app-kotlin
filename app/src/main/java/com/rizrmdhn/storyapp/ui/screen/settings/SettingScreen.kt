package com.rizrmdhn.storyapp.ui.screen.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R

@Composable
fun SettingScreen(
    darkMode: Boolean,
    setDarkMode: (Boolean) -> Unit,
    locale: String,
    setLocale: (String) -> Unit,
    onLogout: () -> Unit,
    navigateBack: () -> Unit
) {
    SettingContent(
        darkMode = darkMode,
        setDarkMode = setDarkMode,
        locale = locale,
        setLocale = setLocale,
        onLogout = onLogout,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    darkMode: Boolean,
    setDarkMode: (Boolean) -> Unit,
    locale: String,
    setLocale: (String) -> Unit,
    onLogout: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.settings))
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigateBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF393E46),
                titleContentColor = Color(0xFFEEEEEE),
                actionIconContentColor = Color(0xFFEEEEEE),
                navigationIconContentColor = Color(0xFFEEEEEE),
            )
        )
    }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(innerPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .drawBehind {
                        val borderSize = 4.dp.toPx()
                        drawLine(
                            color = if (darkMode) Color(0xFFEEEEEE) else Color(0xFF393E46),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = darkMode,
                    onCheckedChange = { setDarkMode(!darkMode) },
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .drawBehind {
                        val borderSize = 4.dp.toPx()
                        drawLine(
                            color = if (darkMode) Color(0xFFEEEEEE) else Color(0xFF393E46),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.change_language),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = locale != "en",
                    onCheckedChange = {
                        setLocale(if (locale == "en") "id" else "en")
                    },
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .drawBehind {
                        val borderSize = 4.dp.toPx()
                        drawLine(
                            color = if (darkMode) Color(0xFFEEEEEE) else Color(0xFF393E46),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize
                        )
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable {
                        onLogout()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    StoryAppTheme {
        SettingScreen(
            darkMode = false,
            setDarkMode = {},
            locale = "en",
            setLocale = {},
            onLogout = {},
            navigateBack = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingScreenDarkPreview() {
    StoryAppTheme {
        SettingScreen(
            darkMode = true,
            setDarkMode = {},
            locale = "en",
            setLocale = {},
            onLogout = {},
            navigateBack = {}
        )
    }
}