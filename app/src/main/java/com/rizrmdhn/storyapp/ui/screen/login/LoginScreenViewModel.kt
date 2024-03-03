package com.rizrmdhn.storyapp.ui.screen.login

import androidx.lifecycle.ViewModel
import com.rizrmdhn.core.utils.FormValidator
import kotlinx.coroutines.flow.MutableStateFlow

class LoginScreenViewModel : ViewModel() {
    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> get() = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> get() = _password

    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: MutableStateFlow<Boolean> get() = _showPassword

    private val _isEmailValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEmailValid: MutableStateFlow<Boolean> get() = _isEmailValid

    private val _emailMessage: MutableStateFlow<String> = MutableStateFlow("")
    val emailMessage: MutableStateFlow<String> get() = _emailMessage

    private val _isPasswordValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPasswordValid: MutableStateFlow<Boolean> get() = _isPasswordValid

    private val _passwordMessage: MutableStateFlow<String> = MutableStateFlow("")
    val passwordMessage: MutableStateFlow<String> get() = _passwordMessage

    private val _initialEmail: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialEmail: MutableStateFlow<Boolean> get() = _initialEmail

    private val _initialPassword: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val initialPassword: MutableStateFlow<Boolean> get() = _initialPassword

    fun setEmail(email: String) {
        if (FormValidator.isEmailValid(email)) {
            _isEmailValid.value = true
            _emailMessage.value = ""
        } else {
            _isEmailValid.value = false
            _emailMessage.value = "Email is not valid"
        }
        _email.value = email
        _initialEmail.value = false
    }

    fun setPassword(password: String) {
        if (FormValidator.isPasswordValid(password)) {
            _isPasswordValid.value = true
            _passwordMessage.value = ""
        } else {
            _isPasswordValid.value = false
            _passwordMessage.value = "Password must be at least 8 characters"
        }
        _password.value = password
        _initialPassword.value = false
    }

    fun setShowPassword(boolean: Boolean) {
        _showPassword.value = boolean
    }

    fun setEmailMessage(message: String) {
        _emailMessage.value = message
    }

    fun setPasswordMessage(message: String) {
        _passwordMessage.value = message
    }

    fun setInitialEmail(boolean: Boolean) {
        _initialEmail.value = boolean
    }

    fun setInitialPassword(boolean: Boolean) {
        _initialPassword.value = boolean
    }
}