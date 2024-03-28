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
import androidx.compose.ui.res.painterResource
import com.rizrmdhn.storyapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    locationSwitch: () -> Unit,
    isLocationOn: Boolean,
    navigateToMap: () -> Unit,
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
                    locationSwitch()
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (isLocationOn) R.drawable.baseline_location_on_24 else R.drawable.baseline_location_off_24
                    ),
                    contentDescription =  "Location"
                )
            }
            if (isLocationOn) {
                IconButton(
                    onClick = {
                        navigateToMap()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.baseline_map_24
                        ),
                        contentDescription = "Info"
                    )
                }
            }
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