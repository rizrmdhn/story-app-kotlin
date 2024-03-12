package com.rizrmdhn.storyapp.ui.screen.addWithLocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.rizrmdhn.core.common.Helpers
import com.rizrmdhn.core.common.Helpers.reduceFileImage
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.storyapp.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddScreenWithLocationViewModel(
    private val storyUseCase: StoryUseCase,
) : ViewModel() {
    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    private val token: StateFlow<String> get() = _token

    private val _uri: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val uri: StateFlow<Uri> get() = _uri

    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    val description: StateFlow<String> get() = _description

    private val _isUploading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> get() = _isUploading

    private val _currentLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLocation: StateFlow<LatLng> get() = _currentLocation

    init {
        getAccessToken()
    }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    private fun setUploadStatus(status: Boolean) {
        _isUploading.value = status
    }

    fun uploadStory(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            val fileData = Helpers.uriToFile(uri.value, context).reduceFileImage()

            if (description.value.isEmpty()) {
                Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@launch
            }
           setUploadStatus(true)

            storyUseCase.addNewStory(fileData, description.value, currentLocation.value.latitude, currentLocation.value.longitude, token.value).catch {
                Toast.makeText(context, "Error When Uploading Stories", Toast.LENGTH_SHORT).show()
                setUploadStatus(false)
            }.collect {
                Toast.makeText(context, "Upload Stories Success", Toast.LENGTH_SHORT).show()
                delay(1000)
                setUploadStatus(false)
                navController.navigate(
                    Screen.Home.route
                ) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }
        }

    }

    fun setCurrentLocation(latLng: LatLng) {
        _currentLocation.value = latLng
    }

     fun getCurrentLocation(context: Context) {
        viewModelScope.launch {
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
                Toast.makeText(context, "Please enable location permission", Toast.LENGTH_SHORT).show()
                return@launch
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    setCurrentLocation(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    fun getAccessToken() {
        viewModelScope.launch {
            storyUseCase.getAccessToken().collect {
                _token.value = "Bearer $it"
            }
        }
    }
}