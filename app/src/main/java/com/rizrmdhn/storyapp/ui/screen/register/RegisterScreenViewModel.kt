package com.rizrmdhn.storyapp.ui.screen.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterScreenViewModel : ViewModel() {
    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: MutableStateFlow<Boolean> get() = _showPassword

    fun setShowPassword(boolean: Boolean) {
        _showPassword.value = boolean
    }
}