package com.rizrmdhn.storyapp.ui.screen.list_maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ListMapScreenViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _currentLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLocation: StateFlow<LatLng> get() = _currentLocation

    private val _myLocation: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val myLocation: StateFlow<LatLng> get() = _myLocation

    private val _stories: MutableStateFlow<Resource<List<Story>>> = MutableStateFlow(Resource.Loading())

    val stories: StateFlow<Resource<List<Story>>> get() = _stories

    fun setCurrentLocation(latLng: LatLng) {
        _currentLocation.value = latLng
    }

    fun getMyLocation(context: Context) {
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
                    setMyLocation(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            _stories.value = Resource.Loading()
            storyUseCase.getStoriesWithLocation().collect {
                _stories.value = it
            }
        }
    }

    private fun setMyLocation(latLng: LatLng) {
        _myLocation.value = latLng
    }
}