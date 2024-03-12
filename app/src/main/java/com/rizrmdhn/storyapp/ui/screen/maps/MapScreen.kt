package com.rizrmdhn.storyapp.ui.screen.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapScreenViewModel = koinViewModel(),
    context: Context = LocalContext.current,
    location: LatLng,
) {
    val currentLocation by viewModel.currentLocation.collectAsState()

    if (location != LatLng(0.0, 0.0)) {
        viewModel.setCurrentLocation(location)
    } else {
        viewModel.getCurrentLocation(context)
    }

    MapScreenContent(
        location = location,
        context = context,
        currentLocation = currentLocation,
        setCurrentLocation = viewModel::setCurrentLocation,
    )
}

@Composable
fun MapScreenContent(
    location: LatLng,
    context: Context,
    currentLocation: LatLng,
    setCurrentLocation: (LatLng) -> Unit,
) {
    val cameraState = rememberCameraPositionState()

    LaunchedEffect(key1 = currentLocation) {
        if (currentLocation != LatLng(0.0, 0.0)) {
            cameraState.position = CameraPosition.fromLatLngZoom(
                currentLocation,
                15f
            )
        }
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraState,
        onMyLocationButtonClick = {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Please enable location permission", Toast.LENGTH_SHORT)
                    .show()
                return@GoogleMap true
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    setCurrentLocation(LatLng(location.latitude, location.longitude))
                }
            }
            return@GoogleMap true
        },
        properties = MapProperties(
            isMyLocationEnabled = location != currentLocation,
        )
    ) {
        if (currentLocation != LatLng(0.0, 0.0)) {
            Marker(
                state = MarkerState(
                    position = currentLocation
                ),
                title = "Current Location",
                snippet = "This is your current location"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultMapScreenPreview() {
    StoryAppTheme {
        MapScreen(
            navController = NavController(LocalContext.current),
            location = LatLng(0.0, 0.0)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkMapScreenPreview() {
    StoryAppTheme(darkTheme = true) {
        MapScreen(
            navController = NavController(LocalContext.current),
            location = LatLng(0.0, 0.0)
        )
    }
}

