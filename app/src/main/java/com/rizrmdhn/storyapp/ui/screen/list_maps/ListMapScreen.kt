package com.rizrmdhn.storyapp.ui.screen.list_maps

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListMapScreen(
    viewModel: ListMapScreenViewModel = koinViewModel(),
) {
    viewModel.stories.collectAsState(initial = Resource.Loading()).value.let { state ->
        when (state) {
            is Resource.Success -> {
                state.data?.let {
                    ListMapScreenContent(
                        setCurrentLocation = viewModel::setCurrentLocation,
                        stories = it
                    )
                }
            }

            is Resource.Error -> {
                Log.e("MapScreen", state.message ?: "An error occurred")
            }

            is Resource.Loading -> {
                // Loading
                viewModel.getStoriesWithLocation()
            }
        }

    }
}

@Composable
fun ListMapScreenContent(
    setCurrentLocation: (LatLng) -> Unit,
    stories: List<Story>,
) {
    val cameraState = rememberCameraPositionState()

    LaunchedEffect(key1 = stories) {
        // zoom to bounds
        if (stories.isNotEmpty()) {
            val builder = LatLngBounds.builder()
            stories.forEach {
                if (it.lat != null && it.lon != null) {
                    builder.include(LatLng(it.lat!!.toDouble(), it.lon!!.toDouble()))
                }
            }
            val bounds = builder.build()
            cameraState.move(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    100
                )
            )
        }
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraState,
        onMapLongClick = {
            setCurrentLocation(it)
            cameraState.position = CameraPosition.fromLatLngZoom(
                it,
                15f
            )
        },
    ) {
        if (stories.isNotEmpty()) {
            stories.forEach {
                if (it.lat != null && it.lon != null) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(it.lat!!.toDouble(), it.lon!!.toDouble())
                        ),
                        title = it.name,
                        snippet = it.description
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultMapScreenPreview() {
    StoryAppTheme {
        ListMapScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkMapScreenPreview() {
    StoryAppTheme(darkTheme = true) {
        ListMapScreen()
    }
}

