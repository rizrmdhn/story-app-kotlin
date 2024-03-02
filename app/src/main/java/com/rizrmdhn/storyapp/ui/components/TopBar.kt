package com.rizrmdhn.storyapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigateToAbout: () -> Unit,
    navigateToSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Story App")
        },
        actions = {
            IconButton(
                onClick = {
                    navigateToAbout()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info"
                )
            }
            IconButton(
                onClick = {
                    navigateToSettings()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
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
}