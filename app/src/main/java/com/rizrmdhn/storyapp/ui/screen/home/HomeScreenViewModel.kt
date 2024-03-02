package com.rizrmdhn.storyapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<Resource<List<Story>>> =
        MutableStateFlow(Resource.Loading())
    val state: StateFlow<Resource<List<Story>>> get() = _state

    private val _storyList = mutableListOf<Story>()
    val storyList: List<Story> get() = _storyList

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    private val token: StateFlow<String> get() = _token

    private val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    private val page: StateFlow<Int> get() = _page

    private val _size: MutableStateFlow<Int> = MutableStateFlow(10)
    private val size: StateFlow<Int> get() = _size

    private val _location: MutableStateFlow<Int> = MutableStateFlow(0)
    private val location: StateFlow<Int> get() = _location

    private val _isFetchingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFetchingMore: StateFlow<Boolean> get() = _isFetchingMore

    private val _loadMore: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val loadMore: StateFlow<Boolean> get() = _loadMore

    fun getStories() {
        viewModelScope.launch {
            _state.value = Resource.Loading()
            getAccessToken()
            storyUseCase.getStories(
                page = page.value,
                size = size.value,
                location = location.value,
                token = token.value
            ).catch {
                _state.value = Resource.Error(it.message.toString())
            }.collect {
                _state.value = it
                val filteredList = it.data?.filter { story ->
                    _storyList.none { filtered -> filtered.id == story.id }
                }
                _storyList.addAll(filteredList?.sortedByDescending { sorted ->
                    sorted.createdAt
                } ?: emptyList()
                )
            }
        }
    }

    fun getMoreStories() {
        viewModelScope.launch {
            _isFetchingMore.value = true
            _page.value += 1
            storyUseCase.getStories(
                page = page.value,
                size = size.value,
                location = location.value,
                token = token.value
            ).catch {
                _state.value = Resource.Error(it.message.toString())
                _isFetchingMore.value = false
            }.collect {
                _state.value = it
                val filteredList = it.data?.filter { story ->
                    _storyList.none { filtered -> filtered.id == story.id }
                }
                _storyList.addAll(filteredList?.sortedByDescending { sorted ->
                    sorted.createdAt
                } ?: emptyList()
                )
                _storyList.addAll(filteredList ?: emptyList())
                if (filteredList != null) {
                    if (filteredList.size < size.value) {
                        _loadMore.value = false
                    }
                }
                _isFetchingMore.value = false
            }
        }
    }

    fun setLoadMore(isLoadMore: Boolean) {
        _loadMore.value = isLoadMore
    }

    fun setFetchingMore(isFetchingMore: Boolean) {
        _isFetchingMore.value = isFetchingMore
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