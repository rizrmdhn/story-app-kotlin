package com.rizrmdhn.storyapp.ui.screen.about

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R

@Composable
fun AboutScreen(
    navigateBack: () -> Unit
) {
    AboutContent(
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.about)
                )
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
            Text(text = "About Screen")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    StoryAppTheme {
        AboutScreen(
            navigateBack = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AboutScreenDarkPreview() {
    StoryAppTheme {
        AboutScreen(
            navigateBack = {}
        )
    }
}