package com.rizrmdhn.storyapp.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.rizrmdhn.core.common.Helpers
import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.storyapp.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryAppViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _initialRender: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialRender: StateFlow<Boolean> get() = _initialRender

    private val _darkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    private val _loginIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginIsLoading: StateFlow<Boolean> get() = _loginIsLoading

    private val _registerIsLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val registerIsLoading: StateFlow<Boolean> get() = _registerIsLoading

    private val _locale: MutableStateFlow<String> = MutableStateFlow("en")
    val locale: StateFlow<String> get() = _locale

    fun getLocaleSetting(context: Context) {
        viewModelScope.launch {
            storyUseCase.getLocaleSetting().collect {
                _locale.value = it
                Helpers.localeSelection(
                    context = context,
                    localeTag = it
                )
            }
        }
    }

    fun setLocaleSetting(locale: String, context: Context) {
        viewModelScope.launch {
            storyUseCase.setLocaleSetting(locale)
            _locale.value = locale
            Helpers.localeSelection(
                context = context,
                localeTag = locale
            )
        }
    }


    fun getDarkMode() {
        viewModelScope.launch {
            storyUseCase.getDarkMode().collect {
                _darkMode.value = it
            }
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            storyUseCase.setDarkMode(isDarkMode)
            _darkMode.value = isDarkMode
        }
    }

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            storyUseCase.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginIsLoading.value = true
                    }

                    is Resource.Success -> {
                        _token.value = result.data?.loginResult?.token ?: ""
                        setAccessToken(result.data?.loginResult?.token ?: "")
                        _loginIsLoading.value = false
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        _loginIsLoading.value = false
                    }
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            storyUseCase.register(name, email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _registerIsLoading.value = true
                    }

                    is Resource.Success -> {
                        _registerIsLoading.value = false
                        Toast.makeText(
                            context,
                            result.data?.message ?: "User Created",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(
                            Screen.Login.route
                        )
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        _registerIsLoading.value = false
                    }
                }
            }
        }
    }

    fun getAccessToken() {
        viewModelScope.launch {
            _initialRender.value = true
            storyUseCase.getAccessToken().collect {
                _token.value = it
                _initialRender.value = false
            }
        }
    }

    private fun setAccessToken(token: String) {
        viewModelScope.launch {
            storyUseCase.setAccessToken(token)
            _token.value = token
        }
    }

    fun logout(
        context: Context
    ) {
        viewModelScope.launch {
            storyUseCase.removeAccessToken()
            _token.value = ""
            Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show()
        }
    }
}