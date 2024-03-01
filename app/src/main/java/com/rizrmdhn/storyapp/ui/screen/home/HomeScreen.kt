package com.rizrmdhn.storyapp.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    token: String,
    removeToken: () -> Unit
) {
    HomeContent(
        token = token,
        removeToken = removeToken
    )
}

@Composable
fun HomeContent(
    token: String,
    removeToken: () -> Unit
) {
    Column {
        Text(text = "Home Screen")
        Text(text = "Token: $token")
        Button(onClick = removeToken) {
            Text(text = "Logout")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        token = "1234567890",
        removeToken = {}
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    HomeScreen(
        token = "1234567890",
        removeToken = {}
    )
}
