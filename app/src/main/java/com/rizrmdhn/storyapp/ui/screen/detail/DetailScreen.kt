package com.rizrmdhn.storyapp.ui.screen.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.StoryDetails
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.ui.components.DetailScreenLoader
import com.rizrmdhn.storyapp.ui.components.ErrorScreen
import com.rizrmdhn.storyapp.ui.components.shimmerBrush
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    storyId: String,
    navController: NavHostController,
    viewModel: DetailScreenViewModel = koinViewModel(),
) {
    viewModel.state.collectAsState(initial = Resource.Loading()).value.let { state ->
        when (state) {
            is Resource.Loading -> {
                viewModel.getAccessToken()
                viewModel.getStoryDetail(storyId)

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Detail Story")
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        navController.popBackStack()
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
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                    ) {
                        DetailScreenLoader()
                    }
                }
            }

            is Resource.Success -> {
                state.data?.let {
                    DetailScreenContent(
                        detailStory = it,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            is Resource.Error -> {
                ErrorScreen(
                    error = state.message.toString(),
                    navController = navController
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    detailStory: StoryDetails,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraState = rememberCameraPositionState()

    LaunchedEffect(key1 = detailStory.lat, key2 = detailStory.lon) {
        if (detailStory.lat != null && detailStory.lon != null) {
            cameraState.position = CameraPosition.fromLatLngZoom(
                LatLng(detailStory.lat!!, detailStory.lon!!),
                15f
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detail Story")
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SubcomposeAsyncImage(
                model = detailStory.photoUrl,
                contentDescription = detailStory.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 150.dp,
                        max = 200.dp
                    )
            ) {
                val state = painter.state
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    shimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = true
                                    )
                                )
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(
                                    min = 150.dp,
                                    max = 200.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent()
                    }

                    is AsyncImagePainter.State.Empty -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(
                                    min = 150.dp,
                                    max = 200.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = detailStory.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detailStory.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LocalDateTime.parse(
                        detailStory.createdAt,
                        DateTimeFormatter.ISO_DATE_TIME
                    ).format(
                        DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (detailStory.lat != null && detailStory.lon != null) {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        cameraPositionState = cameraState,
                    ) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(detailStory.lat!!, detailStory.lon!!)
                            ),
                            title = detailStory.name,
                            snippet = detailStory.description
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    StoryAppTheme {
        DetailScreen(
            storyId = "1",
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenDarkPreview() {
    StoryAppTheme {
        DetailScreen(
            storyId = "1",
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}