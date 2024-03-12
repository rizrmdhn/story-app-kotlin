package com.rizrmdhn.storyapp.ui.screen.addWithLocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizrmdhn.core.common.Helpers
import com.rizrmdhn.core.ui.theme.StoryAppTheme
import com.rizrmdhn.storyapp.R
import com.rizrmdhn.storyapp.ui.components.shimmerBrush
import com.rizrmdhn.storyapp.ui.screen.addWithLocation.AddScreenWithLocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddScreenWithLocation(
    navController: NavHostController,
    viewModel: AddScreenWithLocationViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val imageUri by viewModel.uri.collectAsState()
    val description by viewModel.description.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    viewModel.getAccessToken()
    AddScreenWithLocationContent(
        navigateBack = {
            navController.popBackStack()
        },
        imageUri = imageUri,
        context = context,
        setImagerUri = {
            viewModel.setUri(it)
        },
        description = description,
        onChangeDescription = {
            viewModel.setDescription(it)
        },
        onAddNewStory = {
            viewModel.uploadStory(context, navController)
        },
        currentLocation = currentLocation,
        setCurrentLocation = {
            viewModel.setCurrentLocation(it)
        },
        isUploading = isUploading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenWithLocationContent(
    currentLocation: LatLng,
    setCurrentLocation: (LatLng) -> Unit,
    imageUri: Uri?,
    setImagerUri: (Uri) -> Unit,
    navigateBack: () -> Unit,
    description: String,
    onChangeDescription: (String) -> Unit,
    onAddNewStory: () -> Unit,
    isUploading: Boolean,
    context: Context
) {
    val uri = Helpers.getImageUri(
        context,
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                setImagerUri(uri)
            } else {
                Toast.makeText(context, "No Image Captured", Toast.LENGTH_SHORT).show()
                setImagerUri(Uri.EMPTY)
            }
        }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            setImagerUri(it)
        } else {
            Toast.makeText(context, "Image not found", Toast.LENGTH_SHORT).show()
            setImagerUri(Uri.EMPTY)
        }
    }

    val permissionCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraState = rememberCameraPositionState()

    LaunchedEffect(key1 = currentLocation) {
        if (currentLocation != LatLng(0.0, 0.0)) {
            cameraState.position = CameraPosition.fromLatLngZoom(
                currentLocation,
                15f
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_new_story),
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
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (imageUri != Uri.EMPTY) {
                Arrangement.Top
            } else {
                Arrangement.Center
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (imageUri != Uri.EMPTY) {
                SubcomposeAsyncImage(
                    model = imageUri,
                    contentDescription = "Image",
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
            } else {
                Row {
                    TextButton(
                        onClick = {
                            val permissionCheckResult =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    )
                                } else {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    )
                                }
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                galleryLauncher.launch("image/*")
                            } else {
                                // Request a permission
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionGalleryLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    permissionGalleryLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        },
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.add_image_from_gallery),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                // Request a permission
                                permissionCameraLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.add_image),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = description,
                onValueChange = {
                    onChangeDescription(it)
                },
                label = {
                    Text(
                        text = stringResource(R.string.description)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .height(300.dp)
                    .padding(16.dp),
                cameraPositionState = cameraState,
                onMapClick = {
                    // Do nothing
                    Log.d("AddScreenWithLocation", "onMapClick: $it")
                },
                onMyLocationButtonClick = {
                    val fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(context)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            setCurrentLocation(LatLng(location.latitude, location.longitude))
                        }
                    }
                    return@GoogleMap true
                },
                properties = MapProperties(
                    isMyLocationEnabled = true,
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
            Spacer(modifier = Modifier.height(16.dp))
            if (imageUri != Uri.EMPTY) {
                TextButton(
                    onClick = {
                        if (!isUploading) {
                            onAddNewStory()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isUploading) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                ) {
                    if (isUploading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = stringResource(R.string.post),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddScreenLightPreview() {
    StoryAppTheme {
        AddScreenWithLocation(
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddScreenPreview() {
    StoryAppTheme {
        AddScreenWithLocation(
            navController = NavHostController(
                LocalContext.current
            )
        )
    }
}

