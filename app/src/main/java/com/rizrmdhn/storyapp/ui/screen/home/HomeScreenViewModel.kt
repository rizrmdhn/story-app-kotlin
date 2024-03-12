package com.rizrmdhn.storyapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<PagingData<Story>> = MutableStateFlow(PagingData.empty())
    val state: StateFlow<PagingData<Story>> get() = _state

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    private val token: StateFlow<String> get() = _token

    private val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    private val page: StateFlow<Int> get() = _page

    private val _location: MutableStateFlow<Int> = MutableStateFlow(0)
    val location: StateFlow<Int> get() = _location


    init {
        getLocationSetting()
        getAccessToken()
        getStories()
    }


    private fun getStories() {
        viewModelScope.launch {
            getAccessToken()
            _state.value = PagingData.empty()
            if (location.value == 1) {
                storyUseCase.getStories(
                    page = page.value, location = 1, token = token.value
                ).cachedIn(viewModelScope).catch {
                    _state.value = PagingData.empty()
                }.collect {
                    _state.value = it
                }
            } else {
                storyUseCase.getStories(
                    page = page.value, location = 0, token = token.value
                ).cachedIn(viewModelScope).catch {
                    _state.value = PagingData.empty()
                }.collect {
                    _state.value = it
                }
            }
        }
    }

    fun locationSwitched() {
        viewModelScope.launch {
            if (location.value == 0) {
                _location.value = 1
                storyUseCase.setLocationSetting(1)
            } else {
                _location.value = 0
                storyUseCase.setLocationSetting(0)
            }
            getStories()
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

    private fun getAccessToken() {
        viewModelScope.launch {
            storyUseCase.getAccessToken().catch {
                _token.value = it.message.toString()
            }.collect {
                _token.value = "Bearer $it"
            }
        }
    }
}