package com.rizrmdhn.storyapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizrmdhn.core.ui.theme.StoryAppTheme

@Composable
fun DetailScreenLoader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(
                    min = 150.dp,
                    max = 200.dp
                )
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(modifier = modifier.height(16.dp))
        Box(
            modifier = modifier
                .width(100.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(modifier = modifier.height(16.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(modifier = modifier.height(16.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(modifier = modifier.height(16.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
        Spacer(modifier = modifier.height(16.dp))
        Box(
            modifier = modifier
                .width(100.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    )
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenLoaderPreview() {
    StoryAppTheme {
        DetailScreenLoader()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenLoaderDarkPreview() {
    StoryAppTheme {
        DetailScreenLoader()
    }
}