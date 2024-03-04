package com.rizrmdhn.storyapp.ui.screen.add

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.rizrmdhn.core.common.Helpers
import com.rizrmdhn.core.common.Helpers.reduceFileImage
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.storyapp.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddScreenViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    private val token: StateFlow<String> get() = _token

    private val _uri: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val uri: StateFlow<Uri> get() = _uri

    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    val description: StateFlow<String> get() = _description

    private val _isUploading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> get() = _isUploading

    private val _lat: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val lat: StateFlow<Double> get() = _lat

    private val _long: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val long: StateFlow<Double> get() = _long

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

            storyUseCase.addNewStory(fileData, description.value, null, null, token.value).catch {
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

    fun getAccessToken() {
        viewModelScope.launch {
            storyUseCase.getAccessToken().collect {
                _token.value = "Bearer $it"
            }
        }
    }
}