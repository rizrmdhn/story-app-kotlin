package com.rizrmdhn.storyapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.StoryDetails
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<Resource<StoryDetails>> =
        MutableStateFlow(Resource.Loading())
    val state: StateFlow<Resource<StoryDetails>> get() = _state

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    private val token: StateFlow<String> get() = _token

    private val _location: MutableStateFlow<Int> = MutableStateFlow(0)
    val location: StateFlow<Int> get() = _location

    init {
        getLocationSetting()
    }

    fun getStoryDetail(id: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading()
            storyUseCase.getStoryDetail(id, token.value).catch {
                _state.value = Resource.Error(it.message.toString())
            }.collect {
                _state.value = it
            }
        }
    }

    private fun getLocationSetting() {
        viewModelScope.launch {
            storyUseCase.getLocationSetting().catch {
                _location.value = 0
            }.collect {
                _location.value = it
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